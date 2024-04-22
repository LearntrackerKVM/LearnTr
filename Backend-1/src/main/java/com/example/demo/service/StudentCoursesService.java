package com.example.demo.service;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Assignment;
import com.example.demo.data.CourseDetails;
import com.example.demo.data.Courses;
import com.example.demo.data.Courseschedule;
import com.example.demo.data.Exam;
import com.example.demo.data.StudentCourses;
import com.example.demo.repository.CourseScheduleRepository;
import com.example.demo.repository.CoursesRepository;
import com.example.demo.repository.StudentCoursesRepository;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.InputStream;

@Service
public class StudentCoursesService {

    private static final String[] COLORS = new String[]{"#c4e7f7", "#E6E6FA", "#FADADD", "#FFDAB9", "#98FF98", 
            "#FBDF78", "#F5B3AE", "#F5DEDE", "#B0E0E6", "#E0E0F8", 
            "#DFF0D8", "#FFE4B5", "#D3D3D3", "#F5F5DC", "#D8BFD8", "#cb9a9a"};
    private Map<String, Set<String>> studentColors = new HashMap<>();

    private final StudentCoursesRepository studentCoursesRepository;
    private final CoursesRepository coursesRepository;
    private final CourseScheduleRepository courseScheduleRepository;
    private final AssignmentService assignmentService;
    private final ExamService examService;
  

    @Autowired
    public StudentCoursesService(StudentCoursesRepository studentCoursesRepository, CoursesRepository courseRepository,
    		CourseScheduleRepository courseScheduleRepository, AssignmentService assignmentService, ExamService examService) {
        this.studentCoursesRepository = studentCoursesRepository;
        this.coursesRepository = courseRepository;
        this.courseScheduleRepository = courseScheduleRepository;
        this.assignmentService =assignmentService;
        this.examService =examService;
        
    }


    public StudentCourses saveStudentCourse(String studentId, String courseId, String courseCode) {
        StudentCourses studentCourses = new StudentCourses();
        studentCourses.setStudentId(studentId);
        studentCourses.setCourseId(courseId);

        Courses course = coursesRepository.findByCourseId(courseId);
        if (course == null) {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }

        studentCourses.setCourseName(course.getCourseName());
        studentCourses.setCourseCode(courseCode);
        String color = assignColorToStudent(studentId);
        studentCourses.setColor(color);
        StudentCourses savedStudentCourses = studentCoursesRepository.save(studentCourses);

        // Decrement available slots and save course
        int updatedAvailableSlots = course.getAvailableSlots() - 1;
        if (updatedAvailableSlots < 0) {
            throw new IllegalStateException("Available slots cannot be negative");
        }
        course.setAvailableSlots(updatedAvailableSlots);
        coursesRepository.save(course);

        List<Assignment> assignments = assignmentService.getAssignmentsByCourseId(courseId);
        for (Assignment assignment : assignments ) {
            List<String> questions;
			try {
				questions = extractQuestionNumbersFromDocumentBytes(assignment.getFileContent(), assignment.getFileType());
		         long creationDateInMillis = assignment.getCreatedDate().getTime();
		            long dueDateInMillis = assignment.getDueDate().getTime();
		            long interval = dueDateInMillis - creationDateInMillis;
		            long questionInterval = interval / questions.size();
		           assignmentService.createStudentTasksAndMilestones(studentId, assignment,questions, creationDateInMillis, questionInterval);
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   
        }
        
        List<Exam> exams = examService.getExamsByCourseId(courseId);
        for (Exam exam : exams) {
        	examService.createOrUpdateStudentTask(exam,savedStudentCourses);
        }


        return savedStudentCourses;
    }

    public List<String> extractQuestionNumbersFromDocumentBytes(byte[] fileContent, String fileType) throws IOException {
        if ("PDF".equals(fileType)) {
            return extractQuestionNumbersFromPDFBytes(fileContent);
        } else if ("DOCX".equals(fileType)) {
            return extractQuestionNumbersFromDOCXBytes(fileContent);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    private List<String> extractQuestionNumbersFromPDFBytes(byte[] pdfBytes) throws IOException {
        try (InputStream pdfInputStream = new ByteArrayInputStream(pdfBytes);
             PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return findQuestionNumbers(text);
        }
    }

    private List<String> extractQuestionNumbersFromDOCXBytes(byte[] docxBytes) throws IOException {
        try (InputStream docxInputStream = new ByteArrayInputStream(docxBytes);
             XWPFDocument doc = new XWPFDocument(docxInputStream)) {
            StringBuilder documentText = new StringBuilder();
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                documentText.append(paragraph.getText()).append("\n");
            }
            return findQuestionNumbers(documentText.toString());
        }
    }
    private List<String> findQuestionNumbers(String text) {
        Set<String> questionNumbers = new HashSet<>();

        // Define patterns to match different formats of question numbers
        String[] patterns = {
                "\\((\\w+)\\)",                                  // Match patterns like "(a)", "(b)"
                "(\\d+)\\s*\\[.*\\]",                            // Match patterns like "1 [10pts]", "2 [20 pts]", etc.
                "(?:Question|Problem)\\s*(\\d+)",               // Match patterns like "Question 1", "Question 2", etc.
                "(?:Q|P)\\s*(\\d+)",                             // Match patterns like "Q1", "P2", etc.
                "(\\d+)\\s+(?=\\[)",                             // Match patterns like "1 " followed by "[", etc.
                "Problem\\s*(\\d+)",                             // Match patterns like "Problem 1", "Problem 2", etc.
                "(\\d+)\\.",                                     // Match patterns like "1.", "2.", etc.
                "(\\d+\\.\\d+)"                                  // Match patterns like "1.1", "1.2", etc.
        };

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                questionNumbers.add(matcher.group(1));
            }
        }

        // Convert the set to a list, sort it, and return
        return questionNumbers.stream().sorted().collect(Collectors.toList());
    }

    private String assignColorToStudent(String studentId) {
        if (!studentColors.containsKey(studentId)) {
            studentColors.put(studentId, new HashSet<>());
        }

        Set<String> usedColors = studentColors.get(studentId);
        for (String color : COLORS) {
            if (!usedColors.contains(color)) {
                usedColors.add(color);
                return color;
            }
        }

        // If all colors are used, default to white
        return "#FFFFFF";
    }
public List<CourseDetails> getCourses(String studentId) {
    List<CourseDetails> courseDetailsList = new ArrayList<>();
    List<StudentCourses> courseIds = studentCoursesRepository.findCourseIdsByStudentId(studentId);

    for (StudentCourses courseId : courseIds) {
        Courses course = coursesRepository.findByCourseId(courseId.getCourseId());
        StudentCourses studentCourse = studentCoursesRepository.findByStudentIdAndCourseId(studentId,course.getCourseId());
        if (course != null) {
            // Fetch course schedule details for the course
            List<Courseschedule> courseSchedules = courseScheduleRepository.findByCourseId(courseId.getCourseId());

            // Construct CourseDetails object combining course and course schedule details
            CourseDetails courseDetails = new CourseDetails();
            courseDetails.setCourseId(courseId.getCourseId());
            courseDetails.setCourseName(course.getCourseName());
            courseDetails.setCourseCode(course.getCourseCode());
            courseDetails.setAvailableSlots(course.getAvailableSlots());
            courseDetails.setCourseSchedules(courseSchedules);
            courseDetails.setColor(studentCourse.getColor());

            // Add CourseDetails to the list
            courseDetailsList.add(courseDetails);
        }
    }

    return courseDetailsList;
}
public int countStudentsEnrolledByCreatedId(String createdById) {
    List<Courses> courses = coursesRepository.findCoursesByCreatedByID(createdById);
    if (courses.isEmpty()) {
        return 0; // No courses found for the given createdById
    }
    
    List<String> courseIds = courses.stream()
                                    .map(Courses::getCourseId) // Assuming getId returns the course ID
                                    .collect(Collectors.toList());
    
    return studentCoursesRepository.countByCourseIdIn(courseIds);
}

public int countCoursesEnrolledbyStudentId(String studentId) 
{
    
    return studentCoursesRepository.countByStudentId(studentId);
}
}
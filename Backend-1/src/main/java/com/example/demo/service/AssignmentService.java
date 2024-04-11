package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Assignment;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TasksMilestones;
import com.example.demo.data.User;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;


@Service
public class AssignmentService {

    

    private final AssignmentRepository assignmentRepository;
    private final StudentTasksRepository studentTasksRepository;
    private final TaskMilestonesRepository taskMilestonesRepository;
    private final StudentCoursesRepository studentCoursesRepository; // Assuming this service can fetch students enrolled in a course

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, StudentTasksRepository studentTasksRepository, TaskMilestonesRepository taskMilestonesRepository, StudentCoursesRepository studentCoursesRepository) {
        this.assignmentRepository = assignmentRepository;
        this.studentTasksRepository = studentTasksRepository;
        this.taskMilestonesRepository = taskMilestonesRepository;
        this.studentCoursesRepository = studentCoursesRepository;
    }
    public Assignment createAssignment(Assignment assignment, MultipartFile file) throws IOException {
        // Save the Assignment
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // Extract Questions
        List<String> questions = extractQuestionNumbersFromDocument(file);

        // Calculate interval between creation date and due date
        long creationDateInMillis = savedAssignment.getCreatedDate().getTime();
        long dueDateInMillis = assignment.getDueDate().getTime();
        long interval = dueDateInMillis - creationDateInMillis;

        // Calculate interval between each question
        long questionInterval = interval / questions.size();

        // Fetch students enrolled in the course using the StudentCourses model
        List<StudentCourses> enrollments = studentCoursesRepository.findByCourseId(assignment.getCourseId());

        // Iterate over each enrollment and create StudentTasks and TaskMilestones
        for (StudentCourses enrollment : enrollments) {
            StudentTasks studentTask = new StudentTasks();
            studentTask.setStudentId(enrollment.getStudentId());
            studentTask.setTaskId(savedAssignment.getId());
            studentTask.setCourseId(assignment.getCourseId());
            studentTask.setCourseName(assignment.getCourseName());
            studentTask.setStatus("Not Started");
            studentTask.setTitle(assignment.getAssignmentName());
            studentTask.setTaskType("Assignment");
            studentTask.setDueDate(assignment.getDueDate());
            studentTask.setMilestones(questions.size());
            StudentTasks savedStudentTask = studentTasksRepository.save(studentTask);

            // Initialize milestone due date
            long milestoneDueDate = creationDateInMillis;

            // Create TaskMilestones for each question
            for (int i = 0; i < questions.size(); i++) {
                TasksMilestones milestone = new TasksMilestones();
                milestone.setStudentTaskId(savedStudentTask.getId());
                milestone.setTitle("Question " + (i + 1));
                milestone.setStudentId(savedStudentTask.getStudentId());
                milestone.setDescription("Complete question " + questions.get(i));
                milestone.setIsComplete(false);
                // Calculate milestone due date
                milestoneDueDate += questionInterval;
                milestone.setDueDate(new Date(milestoneDueDate));

                milestone.setStatus("Not Started");
                taskMilestonesRepository.save(milestone);
            }
        }

        return savedAssignment;
    }


    public Iterable<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public List<String> extractQuestionNumbersFromDocument(MultipartFile file) throws IOException {
        String fileType = file.getContentType();

        if ("application/pdf".equals(fileType)) {
            // Handle as PDF
            return extractTextFromPDF(file.getInputStream()); // Use this method for PDF processing
        } else if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType)) {
            // Existing DOCX processing logic
            return extractQuestionNumbersFromDOCX(file.getInputStream()); // Use this method for DOCX
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
    public List<String> extractTextFromPDF(InputStream pdfInputStream) throws IOException {
        List<String> extractedText = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            // Extract and return question numbers from the text
            return findQuestionNumbers(text);
        }
    }
    public List<String> extractQuestionNumbersFromDOCX(InputStream docxInputStream) throws IOException {
        StringBuilder documentText = new StringBuilder();

        try (XWPFDocument doc = new XWPFDocument(docxInputStream)) {
            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                documentText.append(paragraph.getText()).append("\n");
            }
        }

        // Extract and return question numbers from the accumulated text
        return findQuestionNumbers(documentText.toString());
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
    
    public Optional<byte[]> getAssignmentFileContent(String courseId, String title) {
        Optional<Assignment> assignment = assignmentRepository.findByAssignmentNameAndCourseId(title, courseId);
        return assignment.map(a -> {
            try {
                return Base64.getDecoder().decode(a.getFileContent());
            } catch (IllegalArgumentException e) {
                // Log the error or handle it as appropriate for your application
                // Returning empty to indicate failure to decode
                return new byte[0];
            }
        });
    }

    public int countAssignmentsByCreatedId(String createdById) {
        return assignmentRepository.countByCreatedById(createdById);
    }
}
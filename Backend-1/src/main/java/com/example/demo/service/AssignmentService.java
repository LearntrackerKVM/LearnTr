

package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.*;
import com.example.demo.repository.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

@Service
public class AssignmentService {
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private StudentTasksRepository studentTasksRepository;
    @Autowired private TaskMilestonesRepository taskMilestonesRepository;
    @Autowired private StudentCoursesRepository studentCoursesRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;

    public Assignment createAssignment(Assignment assignment, MultipartFile file) throws IOException {
    	   String fileType = file.getContentType();

           if ("application/pdf".equals(fileType)) {
           	assignment.setFileType("PDF");
           } else if ("application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(fileType)) {
        		assignment.setFileType("DOCX");
           } else {
               throw new IllegalArgumentException("Unsupported file type: " + fileType);
           }
        Assignment savedAssignment = assignmentRepository.save(assignment);
        List<String> questions = extractQuestionNumbersFromDocument(file);
        long creationDateInMillis = savedAssignment.getCreatedDate().getTime();
        long dueDateInMillis = assignment.getDueDate().getTime();
        long interval = dueDateInMillis - creationDateInMillis;
        long questionInterval = interval / questions.size();
        List<StudentCourses> enrollments = studentCoursesRepository.findByCourseId(assignment.getCourseId());

     
        // Iterate over each enrollment to send notifications and create tasks
        enrollments.forEach(enrollment -> {
            User student = userRepository.findById(enrollment.getStudentId()).orElse(null);
            if (student != null) {
                String notificationMessage = buildNotificationMessage(savedAssignment, student);
                emailService.sendSimpleMessage(student.getEmail(), "New Assignment Notification", notificationMessage);
            }
            createStudentTasksAndMilestones(enrollment.getStudentId(), savedAssignment, questions, creationDateInMillis, questionInterval);
        });

        return savedAssignment;
    }

    private String buildNotificationMessage(Assignment assignment, User student) {
        return String.format("New Assignment posted for: %s\nBy professor: %s\nAssignment: %s\nDue date: %s\n",
                assignment.getCourseName(), assignment.getCreatedBy(), assignment.getAssignmentName(), assignment.getDueDate());
    }

    public Iterable<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }
    public void createStudentTasksAndMilestones(String studentId, Assignment assignment, List<String> questions, long startMillis, long interval) {
        StudentTasks studentTask = new StudentTasks();
        studentTask.setStudentId(studentId);
        studentTask.setTaskId(assignment.getId());
        studentTask.setCourseId(assignment.getCourseId());
        studentTask.setCourseName(assignment.getCourseName());
        studentTask.setStatus("Not Started");
        studentTask.setTitle(assignment.getAssignmentName());
        studentTask.setTaskType("Assignment");
        studentTask.setDueDate(assignment.getDueDate());
        studentTask.setMilestones(questions.size());
        studentTask.setMilestonesCompleted(0);
        StudentTasks savedStudentTask = studentTasksRepository.save(studentTask);

        long milestoneDueDate = startMillis;
        for (int i = 0; i < questions.size(); i++) {
            TasksMilestones milestone = new TasksMilestones();
            milestone.setStudentTaskId(savedStudentTask.getId());
            milestone.setTitle("Question " + (i + 1));
            milestone.setStudentId(studentId);
            milestone.setDescription("Complete question " + questions.get(i));
            milestone.setIsComplete(false);
            milestoneDueDate += interval;
            milestone.setDueDate(new Date(milestoneDueDate));
            milestone.setStatus("Not Started");
            taskMilestonesRepository.save(milestone);
        }
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
    public List<Assignment> getAssignmentsByCourseId(String courseId) {
        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);
        return assignments.stream().distinct().collect(Collectors.toList());  // Ensure distinct only if absolutely necessary
    }


}

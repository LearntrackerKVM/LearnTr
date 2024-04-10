package com.example.demo.controller;

import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpHeaders;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Assignment;
import com.example.demo.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
@CrossOrigin("*")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

 
    @PostMapping
    public ResponseEntity<?> createAssignment(@RequestParam("assignmentName") String assignmentName,
                                       @RequestParam("assignmentNumber") String assignmentNumber,
                                       @RequestParam("courseId") String courseId,
                                       @RequestParam("courseName") String courseName,
                                       @RequestParam("dueDate") String dueDate,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam("createdDate") String createdDate,
                                       @RequestParam("createdById") String createdById,
                                       @RequestParam("createdBy") String createdBy) {
        try {
        	Assignment assignment = new Assignment(assignmentName, assignmentNumber, courseId, courseName, dueDate, file,createdDate,createdById,createdBy);
            
            
            // Call the service method, including the file
            Assignment createdAssignment = assignmentService.createAssignment(assignment, file);
            
            // Return a successful response
            return ResponseEntity.ok(createdAssignment);
        } catch (IOException e) {
            // Handle the IOException (e.g., return an error response)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }

    @GetMapping
    public Iterable<Assignment> getAllAssignments() {
        return assignmentService.getAllAssignments();
    }
    

    @PostMapping("/questions")
    public ResponseEntity<List<String>> extractQuestionNumbers(@RequestParam("file") MultipartFile file) {
        try {
            List<String> questionNumbers = assignmentService.extractQuestionNumbersFromDocument(file);
            return ResponseEntity.ok().body(questionNumbers);
        } catch (IOException e) {
            // Handle IOException appropriately
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/getNoOfAssignments/{createdById}")
    public ResponseEntity<Integer> getNoOfAssignmentsAddedByProfessor(@PathVariable String createdById) {
        int numberOfAssignments = assignmentService.countAssignmentsByCreatedId(createdById);
        return ResponseEntity.ok(numberOfAssignments);
    }
    
    @GetMapping("/file/{courseId}/{title}")
    public ResponseEntity<byte[]> downloadAssignmentFile(@PathVariable String courseId, @PathVariable String title) {
        Optional<byte[]> fileContent = assignmentService.getAssignmentFileContent(courseId, title);

        return fileContent.map(content -> 
            ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Or specific media type for your file
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + title + "\"")
                .body(content)
        ).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
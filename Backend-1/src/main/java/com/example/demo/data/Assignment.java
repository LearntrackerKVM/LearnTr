package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Document(collection = "assignments")
public class Assignment {
    @Id
    private String id;
    private String assignmentName;
    private String assignmentNumber;
    private String courseId;
    private String courseName;
    private Date dueDate;
    private Date createdDate;
    private String createdById;
    private String createdBy;
    private byte[] fileContent; // byte array to store file content

    public Assignment() {
    }
    public Assignment(String assignmentName, String assignmentNumber, String courseId, String courseName, Date dueDate, byte[] fileContent, Date createdDate, String createdById, String createdBy) {
        this.assignmentName = assignmentName;
        this.assignmentName = assignmentName;
        this.assignmentNumber = assignmentNumber;
        this.courseId = courseId;
        this.courseName = courseName;
        this.dueDate = dueDate;
        this.fileContent = fileContent;
        this.createdDate = createdDate;
        this.createdById = createdById;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    public void setAssignmentNumber(String assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // New constructor to handle file uploads
    public Assignment(String assignmentName, String assignmentNumber, String courseId, String courseName, String dueDate, MultipartFile file, String createdDate, String createdById, String createdBy) {
        this.assignmentName = assignmentName;
        this.assignmentNumber = assignmentNumber;
        this.courseId = courseId;
        this.courseName = courseName;
        try {
            this.dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate); // Parse dueDate string to Date object
            this.fileContent = file.getBytes(); // Set the file content from the multipart file
            this.createdDate = new SimpleDateFormat("yyyy-MM-dd").parse(createdDate);; // Set the createdDate
            this.createdById = createdById; // Set the createdById
            this.createdBy = createdBy; // Set the createdBy
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

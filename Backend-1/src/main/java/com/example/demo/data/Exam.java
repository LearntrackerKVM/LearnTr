package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Document(collection = "exams")
public class Exam {
    @Id
    private String id;
    private int examNumber;
    private String examName;
    private String courseId;
    private String courseCode;
    private String courseName;
    private LocalDate examDate;
    private LocalTime examTime;
    private String roomNumber;
    private String[] syllabus;
    private Date createdDate;
    private String createdById;
    private String createdBy;

    // Constructors
    public Exam() {
    }

    public Exam(int examNumber, String examName, String courseId, String courseCode, String courseName, LocalDate examDate, LocalTime examTime, String roomNumber, String[] syllabus) {
        this.examNumber = examNumber;
        this.examName = examName;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.examDate = examDate;
        this.examTime = examTime;
        this.roomNumber = roomNumber;
        this.syllabus = syllabus;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExamNumber() {
        return examNumber;
    }

    public void setExamNumber(int examNumber) {
        this.examNumber = examNumber;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public LocalTime getExamTime() {
        return examTime;
    }

    public void setExamTime(LocalTime examTime) {
        this.examTime = examTime;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String[] getSyllabus() {
        return syllabus;
    }

    public void setSyllabus(String[] syllabus) {
        this.syllabus = syllabus;
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
}

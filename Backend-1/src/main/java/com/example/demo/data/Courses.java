package com.example.demo.data;

import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courses") // Define the MongoDB collection name if different from the class name
public class Courses {

    @Id
    private String courseId; // MongoDB typically uses String for IDs

    private String courseName;

    private String courseDescription;

    private Integer noOfAssignments;

    private Integer noOfExams;

    private Date startDate;

    private Date endDate;

    private Integer noOfStudentsEnrolled = 0;

    private String createdBy;

    private String createdByID; // Adjusted to String assuming it references an ID from another document

    private Integer capacity;

    private Date createdAt;
    
    private String courseCode;
    
    private String semester;
    
    private Integer availableSlots;
     

    
 // Getters
    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public Integer getNoOfAssignments() {
        return noOfAssignments;
    }

    public Integer getNoOfExams() {
        return noOfExams;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Integer getNoOfStudentsEnrolled() {
        return noOfStudentsEnrolled;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    
    public String getCourseCode() {
    	return courseCode;
    }
    public String getSemester() {
    	return semester;
    }
    public Integer getAvailableSlots() {
        return availableSlots;
    }

    // Setters
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public void setNoOfAssignments(Integer noOfAssignments) {
        this.noOfAssignments = noOfAssignments;
    }

    public void setNoOfExams(Integer noOfExams) {
        this.noOfExams = noOfExams;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNoOfStudentsEnrolled(Integer noOfStudentsEnrolled) {
        this.noOfStudentsEnrolled = noOfStudentsEnrolled;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreatedByID(String createdByID) {
        this.createdByID = createdByID;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setCourseCode(String courseCode) {
    	 this.courseCode = courseCode;
    }
    public void setSemester(String semester) {
    	this.semester = semester;
    }
    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }



}

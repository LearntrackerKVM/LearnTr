package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "studentcourses")
public class StudentCourses {

    @Id
    private String id;

    private String studentCourseId;
    private String courseId;
    private String courseName;
    private String courseCode;
    private String studentId;
    private Date enrolledDate;
    private String createdById;
    private String createdBy;
    private String color;

    public StudentCourses() {
        // Default constructor required by MongoDB
    }

    public StudentCourses(String studentCourseId, String courseId, String courseCode, String studentId, Date enrolledDate, 
    		String createdById, String createdBy, String color) {
        this.studentCourseId = studentCourseId;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.studentId = studentId;
        this.enrolledDate = enrolledDate;
        this.createdById = createdById;
        this.createdBy = createdBy;
        this.setColor(color);
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(String studentCourseId) {
        this.studentCourseId = studentCourseId;
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

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(Date enrolledDate) {
        this.enrolledDate = enrolledDate;
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

    @Override
    public String toString() {
        return "StudentCourses{" +
                "id='" + id + '\'' +
                ", studentCourseId='" + studentCourseId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", studentId='" + studentId + '\'' +
                ", enrolledDate=" + enrolledDate +
                ", createdById='" + createdById + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}



}

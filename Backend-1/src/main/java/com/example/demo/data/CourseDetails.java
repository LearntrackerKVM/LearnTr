package com.example.demo.data;
import java.util.List;



public class CourseDetails {
    private String courseId;
    private String courseName;
    private String courseCode;
    private int availableSlots;
    private List<Courseschedule> courseSchedules;
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public List<Courseschedule> getCourseSchedules() {
        return courseSchedules;
    }

    public void setCourseSchedules(List<Courseschedule> courseSchedules) {
        this.courseSchedules = courseSchedules;
    }
}

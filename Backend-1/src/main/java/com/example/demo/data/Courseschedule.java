package com.example.demo.data;

import java.time.LocalTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "courseschedule")
public class Courseschedule {

    @Id
    private String scheduleId; // MongoDB uses String for IDs

    // Removed @DBRef annotation and replaced Courses object with String courseId
    private String courseId; // Reference to Courses document by ID

    private String dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    // Getters and Setters
    public String getScheduleId() {
        return scheduleId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}

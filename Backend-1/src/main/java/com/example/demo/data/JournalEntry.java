package com.example.demo.data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Document
public class JournalEntry {
    @Id
    private String id;
    private LocalDate date;
    private String studentId;
    private String studentName;
    private String morningMood;
    private String nightMood;
    private List<Habit> habits;
    private List<String> tasks;
    private List<String> priorities;
    private String notes;
    private boolean importantDay;
    private String importantDayDescription;
    private String nightDiary;  // Night diary entry
    private String colorForTheDay;

    // Constructors
    public JournalEntry() {}

    public JournalEntry(String id, LocalDate date, String studentId, String studentName, String morningMood, String nightMood, List<Habit> habits, List<String> tasks, List<String> priorities, String notes, boolean importantDay, String importantDayDescription, String nightDiary,String colorForTheDay) {
        this.id = id;
        this.date = date;
        this.studentId = studentId;
        this.studentName = studentName;
        this.morningMood = morningMood;
        this.nightMood = nightMood;
        this.habits = habits;
        this.tasks = tasks;
        this.priorities = priorities;
        this.notes = notes;
        this.importantDay = importantDay;
        this.importantDayDescription = importantDayDescription;
        this.nightDiary = nightDiary;
        this.colorForTheDay = colorForTheDay;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMorningMood() {
        return morningMood;
    }

    public void setMorningMood(String morningMood) {
        this.morningMood = morningMood;
    }

    public String getNightMood() {
        return nightMood;
    }

    public void setNightMood(String nightMood) {
        this.nightMood = nightMood;
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public void setHabits(List<Habit> habits) {
        this.habits = habits;
    }

    public List<String> getTasks() {
        return tasks;
    }

    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }

    public List<String> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<String> priorities) {
        this.priorities = priorities;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean importantDay() {
        return importantDay;
    }

    public void setImportantDay(boolean importantDay) {
        importantDay = importantDay;
    }

    public String getImportantDayDescription() {
        return importantDayDescription;
    }

    public void setImportantDayDescription(String importantDayDescription) {
        this.importantDayDescription = importantDayDescription;
    }

    public String getNightDiary() {
        return nightDiary;
    }

    public void setNightDiary(String nightDiary) {
        this.nightDiary = nightDiary;
    }
    public String getColorForTheDay() {
        return colorForTheDay;
    }

    public void setColorForTheDay(String colorForTheDay) {
        this.colorForTheDay = colorForTheDay;
    }
}

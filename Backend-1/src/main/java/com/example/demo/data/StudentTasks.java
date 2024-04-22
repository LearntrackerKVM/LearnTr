package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "studentTasks")
public class StudentTasks {

    @Id
    private String id; // Unique ID for the document

    private String studentId; // ID of the student
    private String taskId; // ID of the task (assignment/exam)
    private String courseId; // ID of the course
    private String status; // Status of the task (e.g., "Not Started", "In Progress", "Completed")
    private Date dueDate; // Due date of the task
    private Date completionDate; // Actual completion date
    private String taskType; // Type of task (e.g., "Assignment", "Exam")
    private String title; // Title of the task
    private String description; // Description of the task
    private String difficultyLevel; // E.g., "Easy", "Medium", "Hard"
    private String notes; // Notes about the task, shared upon completion
    private Integer milestones;
    private String courseName;
    private Integer milestonesCompleted;
    // Constructor
    public StudentTasks() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getStatus() {
        return status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

	public Integer getMilestones() {
		return milestones;
	}

	public void setMilestones(Integer milestones) {
		this.milestones = milestones;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Integer getMilestonesCompleted() {
		return milestonesCompleted;
	}

	public void setMilestonesCompleted(Integer milestonesCompleted) {
		this.milestonesCompleted = milestonesCompleted;
	}
}

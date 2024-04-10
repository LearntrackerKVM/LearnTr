package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "taskMilestones")
public class TasksMilestones {

    @Id
    private String id; // Unique ID for the milestone document

    private String studentTaskId; // ID of the StudentTask this milestone is associated with
    private String title; // Title of the milestone
    private String description; // Description of the milestone
    private Date dueDate; // Due date for completing the milestone
    private String status; // Status of the milestone (e.g., "Not Started", "In Progress", "Completed")
    private String notes; // Additional notes by the student or instructor
    private Date completionDate; // Actual date when the milestone was completed
    private String difficultyLevel;
    private Integer sequenceNumber;
    private Boolean isComplete;
    private String studentId;
    
    // Constructor
    public TasksMilestones() {
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getStudentTaskId() {
        return studentTaskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStudentTaskId(String studentTaskId) {
        this.studentTaskId = studentTaskId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}

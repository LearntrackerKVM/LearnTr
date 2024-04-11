package com.example.demo.data;

public class MilestoneDto {
    private String id;
    private String title;
    private String difficultyLevel;
    private String notes;
    private String status;

    // Constructor
    public MilestoneDto(String id, String title, String difficultyLevel, String notes, String status) {
        this.setId(id);
        this.setTitle(title);
        this.setDifficultyLevel(difficultyLevel);
        this.setNotes(notes);
        this.setStatus(status);
    }

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(String difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    // Getters and Setters
    // Implement getters and setters here
}
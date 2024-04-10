package com.example.demo.data;

import java.util.List;

public class TaskDto {
    private String taskId;
    private String taskTitle;
    private String taskType;
    private String difficultyLevel;
    private String notes;
    private String status;
    private List<MilestoneDto> milestones;
    private Integer noofMilestones;
    private String milestonesCompletedOutOfTotal;
    private String studentFirstName;
    private String studentLastName;
    private String studentEmail;
    private String studentId;
    // Constructor, getters, and setters
    public TaskDto(String taskId, String taskTitle, String taskType, String difficultyLevel, String notes, String status, List<MilestoneDto> milestones) {
        this.setTaskId(taskId);
        this.setTaskTitle(taskTitle);
        this.setTaskType(taskType);
        this.setDifficultyLevel(difficultyLevel);
        this.setNotes(notes);
        this.setStatus(status);
        this.setMilestones(milestones);
    }
    public TaskDto(String taskId, String taskTitle, String taskType, String difficultyLevel, String notes, String status, List<MilestoneDto> milestones, String milestonesCompletedOutOfTotal) {
        this.setTaskId(taskId);
        this.setTaskTitle(taskTitle);
        this.setTaskType(taskType);
        this.setDifficultyLevel(difficultyLevel);
        this.setNotes(notes);
        this.setStatus(status);
        this.setMilestones(milestones);
        this.setMilestonesCompletedOutOfTotal(milestonesCompletedOutOfTotal);
    }

    public TaskDto(String taskId, String taskTitle, String taskType, String difficultyLevel, String notes, 
            String status, List<MilestoneDto> milestones, String milestonesCompletedOutOfTotal,
            String studentFirstName, String studentLastName, String studentEmail,String studentId) {
        this.setTaskId(taskId);
        this.setTaskTitle(taskTitle);
        this.setTaskType(taskType);
        this.setDifficultyLevel(difficultyLevel);
        this.setNotes(notes);
        this.setStatus(status);
        this.setMilestones(milestones);
        this.setMilestonesCompletedOutOfTotal(milestonesCompletedOutOfTotal);
        this.setStudentFirstName(studentFirstName);
        this.setStudentLastName(studentLastName);
        this.setStudentEmail(studentEmail);
        this.setStudentId(studentId);

}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getNoofMilestones() {
		return noofMilestones;
	}

	public void setNoofMilestones(Integer noofMilestones) {
		this.noofMilestones = noofMilestones;
	}

	public List<MilestoneDto> getMilestones() {
		return milestones;
	}

	public void setMilestones(List<MilestoneDto> milestones) {
		this.milestones = milestones;
	}
	public String getMilestonesCompletedOutOfTotal() {
		return milestonesCompletedOutOfTotal;
	}
	public void setMilestonesCompletedOutOfTotal(String milestonesCompletedOutOfTotal) {
		this.milestonesCompletedOutOfTotal = milestonesCompletedOutOfTotal;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentFirstName() {
		return studentFirstName;
	}
	public void setStudentFirstName(String studentFirstName) {
		this.studentFirstName = studentFirstName;
	}
	public String getStudentLastName() {
		return studentLastName;
	}
	public void setStudentLastName(String studentLastName) {
		this.studentLastName = studentLastName;
	}
	public String getStudentEmail() {
		return studentEmail;
	}
	public void setStudentEmail(String studentEmail) {
		this.studentEmail = studentEmail;
	}

    // Getters and Setters
    // Implement getters and setters here
}


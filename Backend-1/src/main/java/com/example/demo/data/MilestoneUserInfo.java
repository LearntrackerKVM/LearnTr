package com.example.demo.data;


public class MilestoneUserInfo {
    private TasksMilestones milestone;
    private User user;
    
    // Constructor, Getters and Setters
    public MilestoneUserInfo(TasksMilestones milestone, User user) {
        this.setMilestone(milestone);
        this.setUser(user);
    }
    // Assume getters and setters are here

	public TasksMilestones getMilestone() {
		return milestone;
	}

	public void setMilestone(TasksMilestones milestone) {
		this.milestone = milestone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}

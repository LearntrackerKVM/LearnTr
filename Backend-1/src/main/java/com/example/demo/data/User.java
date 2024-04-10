package com.example.demo.data;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id; // MongoDB typically uses String for IDs

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String password;

    private String role;
    
    private byte[] profilePicture; // Store the profile picture directly as binary data
    private String aboutMe;
    private String badge; // Represents the current badge of the user
    private int milestonesCompleted; // The number of milestones the user has completed
    private int courses; 
    private int rank; // Add ranking field as an integer

    // Getters and Setters

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Getter and setter for the aboutMe field
    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", profilePicture=[binary data]" + // For simplicity in the toString representation
                ", aboutMe='" + aboutMe + '\'' +
                ", badge='" + badge + '\'' +
                ", milestonesCompleted=" + milestonesCompleted +
                ", courses=" + courses +
                      ", rank=" + rank +
                '}';
    }

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public int getMilestonesCompleted() {
		return milestonesCompleted;
	}

	public void setMilestonesCompleted(int milestonesCompleted) {
		this.milestonesCompleted = milestonesCompleted;
	}

	public int getCourses() {
		return courses;
	}

	public void setCourses(int courses) {
		this.courses = courses;
	}

}

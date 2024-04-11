package com.example.demo.data;


public class FriendRequestDTO {
    private String id; // Friend request ID
    private String senderId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;

    // Constructor, getters, and setters
    public FriendRequestDTO(FriendRequest request, String firstName, String lastName, String email) {
        this.setId(request.getId());
        this.setSenderId(request.getSenderId());
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setStatus(request.getStatus());
    }


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}


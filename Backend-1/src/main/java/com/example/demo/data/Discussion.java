package com.example.demo.data;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "discussions")
public class Discussion {
    @Id
    private String id; 
    private String parentID; // Can be null for top-level posts
    private String courseID;
    private String authorID;
    private String authorName;
    private String contentType; // e.g., "Question", "Comment"
    private String title; // Can be null for comments
    private String content;
    private Date timestamp;
    private String attachments; // This could be a path or URL. Consider changing the type based on your requirements
    private String status; // e.g., "Open", "Answered"
    private Integer likes;
    private Integer dislikes;
    private String role; // e.g., "Student", "Teacher"
    
    // Constructor
    public Discussion( String parentID, String courseID, String authorID, String contentType, 
                      String title, String content, Date timestamp, String attachments, String status, 
                      Integer likes, Integer dislikes, String role) {
        this.setParentID(parentID);
        this.setCourseID(courseID);
        this.setAuthorID(authorID);
        this.setContentType(contentType);
        this.setTitle(title);
        this.setContent(content);
        this.setTimestamp(timestamp);
        this.setAttachments(attachments);
        this.setStatus(status);
        this.setLikes(likes);
        this.setDislikes(dislikes);
        this.setRole(role);
    }
    


	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getAuthorID() {
		return authorID;
	}

	public void setAuthorID(String authorID) {
		this.authorID = authorID;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getId() {
	    return id;
	}



	public String getAuthorName() {
		return authorName;
	}



	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

    // Add the rest of the getters and setters following the pattern above for each field
    
}

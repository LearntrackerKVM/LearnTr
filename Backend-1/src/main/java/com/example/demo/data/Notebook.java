package com.example.demo.data;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notebook" )
public class Notebook {

    private String id;
    private String studentId;
    private String studentName;
    private String notes;
    private Date modifiedDate; 
    
    public Notebook() {
    }

	public String getId() {
		return id;
	}
	
	
	public void setId(String id) {
		this.id = id;
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}

package com.example.demo.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Habit {

	

	    @Id
	    private String id;
	    private String name;
	    private boolean completed;
	    private String studentId; // Link habit to a specific student
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean completed() {
			return completed;
		}
		public void setCompleted(boolean completed) {
			this.completed = completed;
		}
		public String getStudentId() {
			return studentId;
		}
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}

	    // Constructors, getters and setters
	}


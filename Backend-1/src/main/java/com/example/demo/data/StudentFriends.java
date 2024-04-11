package com.example.demo.data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "studentfriends")
public class StudentFriends {

    @Id
    private String id;

    private String studentId;
    private String studentName;

    private List<Friend> friends;

    
    public StudentFriends(String studentId, String studentName, List<Friend> friends) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.friends = friends;
    }
public StudentFriends() {
	
}
    // Getters and setters
    // Constructors
    // Other methods as needed

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

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }
}


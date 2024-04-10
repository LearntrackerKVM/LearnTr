package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.StudentFriends;
import com.example.demo.data.StudentFriendsRequest;
import com.example.demo.data.User;
import com.example.demo.service.StudentFriendsService;
@RestController
@RequestMapping("/api/studentfriends")
@CrossOrigin("*")
public class StudentFriendsController {

    private final StudentFriendsService studentFriendsService;

    @Autowired
    public StudentFriendsController(StudentFriendsService studentFriendsService) {
        this.studentFriendsService = studentFriendsService;
    }

    @PostMapping
    public ResponseEntity<?> addFriendship(@RequestBody StudentFriendsRequest request) {
        try {
            StudentFriends studentFriends = studentFriendsService.addFriendship(
                    request.getStudentId(),
                    request.getFriendId(),
                    request.getFriendFirstName(),
                    request.getFriendLastName(),
                    request.getFriendEmail()
            );
            return new ResponseEntity<>(studentFriends, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add friendship: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{studentId}/friends")
    public ResponseEntity<?> getFriendsWithUserDataByStudentId(@PathVariable String studentId) {
        try {
            List<User> friends = studentFriendsService.getFriendsWithUserDataByStudentId(studentId);
            return new ResponseEntity<>(friends, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve friends' data: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}

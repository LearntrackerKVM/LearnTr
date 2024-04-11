package com.example.demo.controller;

import com.example.demo.data.User;
import com.example.demo.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@CrossOrigin("*")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    // Get all students by ranking in ascending order
    @GetMapping("/students/rank")
    public ResponseEntity<List<User>> getAllStudentsByRanking() {
        List<User> students = leaderboardService.getAllStudentsByRanking();
        return ResponseEntity.ok(students);
    }

    // Get all friends of the student by studentId
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<User>> getStudentFriends(@PathVariable String userId) {
        List<User> friends = leaderboardService.getStudentFriends(userId);
        return ResponseEntity.ok(friends);
    }

    // Get all students with the same courses as the student by studentId
    @GetMapping("/courses/same/{userId}")
    public ResponseEntity<List<User>> getStudentsWithSameCourses(@PathVariable String userId) {
        List<User> studentsWithSameCourses = leaderboardService.getStudentsWithSameCourses(userId);
        return ResponseEntity.ok(studentsWithSameCourses);
    }

    // Get friends with the same courses as the student by studentId
    @GetMapping("/friends/courses/same/{userId}")
    public ResponseEntity<List<User>> getFriendsWithSameCourses(@PathVariable String userId) {
        List<User> friendsWithSameCourses = leaderboardService.getFriendsWithSameCourses(userId);
        return ResponseEntity.ok(friendsWithSameCourses);
    }
}

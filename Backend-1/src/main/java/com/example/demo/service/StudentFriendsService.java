package com.example.demo.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Friend;
import com.example.demo.data.StudentFriends;
import com.example.demo.data.User;
import com.example.demo.repository.StudentFriendsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class StudentFriendsService {

    private final StudentFriendsRepository studentFriendsRepository;
    private final UserRepository userRepository; // Inject UserRepository

    @Autowired
    public StudentFriendsService(StudentFriendsRepository studentFriendsRepository, UserRepository userRepository) {
        this.studentFriendsRepository = studentFriendsRepository;
        this.userRepository = userRepository;
    }

    public StudentFriends addFriendship(String studentId, String friendId, String friendFirstName, String friendLastName, String friendEmail) {
        // Check if a record exists for the student ID
        List<StudentFriends> existingRecords = studentFriendsRepository.findByStudentId(studentId);

        if (!existingRecords.isEmpty()) {
            // If a record exists, update it with the new friend's information
            StudentFriends studentFriends = existingRecords.get(0);
            List<Friend> friends = studentFriends.getFriends();

            // Add the new friend to the existing record
            Friend friend = new Friend();
            friend.setId(friendId);
            friend.setFirstName(friendFirstName);
            friend.setLastName(friendLastName);
            friend.setEmail(friendEmail);
            friends.add(friend);

            // Update the record in the repository
            studentFriends.setFriends(friends);

            return studentFriendsRepository.save(studentFriends);
        } else {
            // If no record exists, create a new one
            StudentFriends studentFriends = new StudentFriends();
            studentFriends.setStudentId(studentId);
            studentFriends.setFriends(Collections.emptyList()); // Initialize friends list

            // Create a new friend
            Friend friend = new Friend();
            friend.setId(friendId);
            friend.setFirstName(friendFirstName);
            friend.setLastName(friendLastName);
            friend.setEmail(friendEmail);

            studentFriends.setFriends(Collections.singletonList(friend));

            return studentFriendsRepository.save(studentFriends);
        }
    }

    public List<User> getFriendsWithUserDataByStudentId(String studentId) {
        List<StudentFriends> friends = studentFriendsRepository.findByStudentId(studentId);
        List<String> friendIds = friends.stream()
                .flatMap(friend -> friend.getFriends().stream())
                .map(Friend::getId)
                .distinct() // Ensure unique IDs
                .collect(Collectors.toList());


        return userRepository.findUsersByIds(friendIds);
    }
}

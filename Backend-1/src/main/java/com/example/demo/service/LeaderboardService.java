package com.example.demo.service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Friend;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentFriends;
import com.example.demo.data.User;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentFriendsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class LeaderboardService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private StudentFriendsRepository studentFriendsRepository;
	
	@Autowired
	private StudentCoursesRepository studentCoursesRepository;
	
	  public List<User> getAllStudentsByRanking() {
	        // Fetch all students from the repository
	        List<User> allStudents = userRepository.findByRole("Student");

	        // Sort the students by ranking
	        List<User> studentsByRanking = allStudents.stream()
	                .sorted(Comparator.comparing(User::getRank))
	                .collect(Collectors.toList());

	        return studentsByRanking;
	    }

	public List<User> getStudentFriends(String userId) {
	    List<StudentFriends> studentFriendsList = studentFriendsRepository.findByStudentId(userId);
	    Set<String> friendIds = studentFriendsList.stream()
	                                               .flatMap(studentFriend -> studentFriend.getFriends().stream())
	                                               .map(Friend::getId) // Map Friend objects to their ID strings
	                                               .collect(Collectors.toSet());

	    return userRepository.findAllById(friendIds).stream()
	                         .filter(user -> "Student".equals(user.getRole()))
	                         .collect(Collectors.toList());
	}


	public List<User> getStudentsWithSameCourses(String userId) {
	    // Fetch courseIds for the requesting user
	    List<String> courseIds = studentCoursesRepository.findByStudentId(userId)
	                                                     .stream()
	                                                     .map(StudentCourses::getCourseId)
	                                                     .collect(Collectors.toList());

	    // Fetch studentIds for those courses
	    Set<String> studentIds = studentCoursesRepository.findByCourseIdIn(courseIds)
	                                                     .stream()
	                                                     .map(StudentCourses::getStudentId)
	                                                     .collect(Collectors.toSet());

	    // Fetch and return User objects for those studentIds, filtering by role "student"
	    return userRepository.findAllById(studentIds).stream()
	                         .filter(user -> "Student".equals(user.getRole()))
	                         .collect(Collectors.toList());
	}

	public List<User> getFriendsWithSameCourses(String userId) {
	    // Step 1: Get the user's friends' IDs
	    Set<String> friendIds = studentFriendsRepository.findFriendIdsByStudentId(userId);
	    
	    // Step 2: Get courseIds for the requesting user
	    List<String> courseIds = studentCoursesRepository.findByStudentId(userId)
	                                                     .stream()
	                                                     .map(StudentCourses::getCourseId)
	                                                     .collect(Collectors.toList());

	    // Step 3: Fetch studentIds enrolled in the same courses who are also friends
	    Set<String> studentIdsWithSameCourses = studentCoursesRepository.findByCourseIdIn(courseIds)
	                                                                    .stream()
	                                                                    .map(StudentCourses::getStudentId)
	                                                                    .filter(friendIds::contains) // Filter to include only friends
	                                                                    .collect(Collectors.toSet());

	    // Step 4: Fetch and return User objects for those studentIds, filtering by role "student"
	    return userRepository.findAllById(studentIdsWithSameCourses).stream()
	                         .filter(user -> "Student".equals(user.getRole()))
	                         .collect(Collectors.toList());
	}


}

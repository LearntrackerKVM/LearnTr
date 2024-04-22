package com.example.demo.service;

import com.example.demo.data.CourseDto;
import com.example.demo.data.Courses;
import com.example.demo.data.Friend;
import com.example.demo.data.MilestoneDto;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentFriends;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TaskDto;
import com.example.demo.data.TasksMilestones;
import com.example.demo.data.User;
import com.example.demo.repository.CoursesRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentFriendsRepository;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class CoursesService {


    private static final String[] COLORS = new String[]{"#c4e7f7", "#E6E6FA", "#FADADD", "#FFDAB9", "#98FF98", 
            "#FBDF78", "#F5B3AE", "#F5DEDE", "#B0E0E6", "#E0E0F8", 
            "#DFF0D8", "#FFE4B5", "#D3D3D3", "#F5F5DC", "#D8BFD8", "#cb9a9a"};
    private Map<String, Set<String>> colorsprof = new HashMap<>();
    
 private final CoursesRepository courseRepository;
 private final StudentCoursesRepository studentCoursesRepository;
 private final UserRepository userRepository;
 private final StudentTasksRepository studentTasksRepository;
 private final TaskMilestonesRepository taskMilestonesRepository;
 private final StudentFriendsRepository studentFriendsRepository;
 

 @Autowired
 public CoursesService(CoursesRepository courseRepository,StudentCoursesRepository studentCoursesRepository, UserRepository userRepository
		 ,StudentTasksRepository studentTasksRepository,TaskMilestonesRepository taskMilestonesRepository,StudentFriendsRepository studentFriendsRepository) {
     this.courseRepository = courseRepository;
     this.studentCoursesRepository = studentCoursesRepository;
     this.userRepository = userRepository;
     this.studentTasksRepository = studentTasksRepository;
     this.taskMilestonesRepository = taskMilestonesRepository;
     this.studentFriendsRepository = studentFriendsRepository;
 }




 public Courses saveNewCourse(Courses course) {
     // Generate courseCode based on the course name and existing courses
     String courseCode = generateCourseCode(course.getCourseName());
     course.setCourseCode(courseCode);
     String color = assignColorToStudent(course.getCreatedByID());
     course.setColor(color);
     // Save the course with the new courseCode
     Courses savedCourse = courseRepository.save(course);
     return savedCourse;
 }

 private String generateCourseCode(String courseName) {
     if (courseName == null || courseName.length() < 2) {
         throw new IllegalArgumentException("Course name must be at least 2 characters long");
     }
     
     // Extract the first two letters of the course name as the base for the course code
     String baseCode = courseName.substring(0, 2).toUpperCase();
     
     // Find the highest number used for courses with the same starting letters
     // This is a placeholder logic, you'll need to implement the actual database query
     Integer highestNumber = findHighestNumberForBaseCode(baseCode);
     int nextNumber = (highestNumber != null) ? highestNumber + 1 : 101;
     
     // Combine the base code with the next available number
     return baseCode + nextNumber;
 }


 private String assignColorToStudent(String profId) {
     if (!colorsprof.containsKey(profId)) {
    	 colorsprof.put(profId, new HashSet<>());
     }

     Set<String> usedColors = colorsprof.get(profId);
     for (String color : COLORS) {
         if (!usedColors.contains(color)) {
             usedColors.add(color);
             return color;
         }
     }

     // If all colors are used, default to white
     return "#FFFFFF";
 }
 private Integer findHighestNumberForBaseCode(String baseCode) {
     Pattern pattern = Pattern.compile("^" + baseCode + "(\\d{3})$");
     int highestNumber = 0;

     // Fetch courses with codes starting with the base code
     List<Courses> courses = courseRepository.findByCourseCodeStartingWith(baseCode + "[0-9]{3}");

     for (Courses course : courses) {
         Matcher matcher = pattern.matcher(course.getCourseCode());
         if (matcher.find()) {
             // Extract the numeric part and compare
             int number = Integer.parseInt(matcher.group(1));
             if (number > highestNumber) {
                 highestNumber = number;
             }
         }
     }

     return highestNumber == 0 ? null : highestNumber;
 }

 
 public Iterable<Courses> findAll() {
     return courseRepository.findAll();
 }

 
 public List<Courses> findCoursesByCreatedId(String createdByID) {
     List<Courses> courses = courseRepository.findByCreatedByID(createdByID);
     return courses; 
 }

 public long countCoursesByCreatedId(String createdById) {
     return courseRepository.countByCreatedByID(createdById);
 }
 
 public List<User> getAllStudentsEnrolledInCoursesAddedByProfessor(String professorId) {
     // Step 1: Retrieve Courses Added by Professor
     List<Courses> coursesAddedByProfessor = courseRepository.findByCreatedByID(professorId);

     // Step 2: Retrieve Students Enrolled in Those Courses
     List<String> courseIds = coursesAddedByProfessor.stream()
             .map(Courses::getCourseId)
             .collect(Collectors.toList());

     List<StudentCourses> studentCourses = studentCoursesRepository.findByCourseIdIn(courseIds);

     // Step 3: Collect and Return the Data
     List<String> studentIds = studentCourses.stream()
             .map(StudentCourses::getStudentId)
             .collect(Collectors.toList());

     // Assuming you have a UserRepository to retrieve users by their IDs
     return userRepository.findUsersByIds(studentIds);
 }


 public List<CourseDto> listCoursesWithTasksAndMilestonesForFriendsByCourseId(String courseId, String studentId) {
     // Fetch the course by courseId
     Courses course = courseRepository.findByCourseId(courseId);
     if (course == null) {
         System.out.println("Course not found for courseId: " + courseId);
         return Collections.emptyList();
     }

     // Fetch the student's friends
     List<StudentFriends> friendDataList = studentFriendsRepository.findByStudentId(studentId);
     if (friendDataList == null || friendDataList.isEmpty()) {
         System.out.println("No friends found for studentId: " + studentId);
         return Collections.emptyList();
     }
     StudentFriends friendData = friendDataList.get(0); // Assuming we're only interested in the first entry
     Set<String> friendIds = friendData.getFriends().stream()
                                        .map(Friend::getId)
                                        .collect(Collectors.toSet());

     // Filter courses enrolled by friends
     List<StudentCourses> enrolledFriendsCourses = studentCoursesRepository.findByCourseId(courseId).stream()
              .filter(enrollment -> friendIds.contains(enrollment.getStudentId()))
              .collect(Collectors.toList());

     // Process enrolled friends for course details
     List<CourseDto> coursesWithTasksAndMilestones = enrolledFriendsCourses.stream().map(enrollment -> {
         List<StudentTasks> tasks = studentTasksRepository.findByCourseIdAndStudentId(courseId, enrollment.getStudentId());
      // Within your stream operation that creates TaskDto objects
         List<TaskDto> taskDtos = tasks.stream().map(task -> {
             List<TasksMilestones> milestones = taskMilestonesRepository.findByStudentTaskId(task.getId());
             long completedMilestones = milestones.stream().filter(TasksMilestones::getIsComplete).count();

             List<MilestoneDto> milestoneDtos = milestones.stream().map(milestone ->
                     new MilestoneDto(milestone.getId(), milestone.getTitle(), milestone.getDifficultyLevel(), milestone.getNotes(), milestone.getStatus())
             ).collect(Collectors.toList());

             String milestonesCompletedOutOfTotal = String.format("%d/%d", completedMilestones, milestones.size());

             // Assuming you can fetch a User object by studentId
             Optional<User> studentOptional = userRepository.findById(task.getStudentId());

             String id = studentOptional.map(User::getId).orElse(null);
             String firstName = studentOptional.map(User::getFirstName).orElse("N/A");
             String lastName = studentOptional.map(User::getLastName).orElse("N/A");
             String email = studentOptional.map(User::getEmail).orElse("N/A");


             return new TaskDto(task.getTaskId(), task.getTitle(), task.getTaskType(), task.getDifficultyLevel(), 
                                task.getNotes(), task.getStatus(), milestoneDtos, milestonesCompletedOutOfTotal,
                                firstName, lastName, email,id);
         }).collect(Collectors.toList());


         return new CourseDto(course.getCourseId(), course.getCourseName(), course.getCourseCode(), taskDtos);
     }).collect(Collectors.toList());

     return coursesWithTasksAndMilestones;
 }

}

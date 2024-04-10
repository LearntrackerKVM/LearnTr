package com.example.demo.controller;


import com.example.demo.data.CourseDto;
import com.example.demo.data.Courses;
import com.example.demo.data.User;
import com.example.demo.service.CoursesService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class CoursesController {

 private final CoursesService coursesService;

 @Autowired
 public CoursesController(CoursesService coursesService) {
     this.coursesService = coursesService;
 }

 @PostMapping("/RegisterCourse")
 public ResponseEntity<Courses> createCourseWithSchedules(@RequestBody Courses courses) {
     Courses savedCourse = coursesService.saveNewCourse(courses);
     return ResponseEntity.ok(savedCourse);
 }
 
 @GetMapping("/getallCourses")
 public Iterable<Courses> getAllCourses() {
     return coursesService.findAll();
 }

 @GetMapping("/getCoursesbyId/{createdById}")
 public ResponseEntity<List<Courses>> getCoursesByCreator(@PathVariable String createdById) {
     List<Courses> courses = coursesService.findCoursesByCreatedId(createdById);
     return ResponseEntity.ok(courses);
 }

 @GetMapping("/getNoOfCourses/{createdById}")
 public ResponseEntity<Long> getNoOfCoursesAddedByProfessor(@PathVariable String createdById) {
     long numberOfCourses = coursesService.countCoursesByCreatedId(createdById);
     return ResponseEntity.ok(numberOfCourses);
 }

 
 @GetMapping("/enrolled/{professorId}")
 public ResponseEntity<List<User>> getAllStudentsEnrolledInCoursesAddedByProfessor(@PathVariable String professorId) {
     List<User> students = coursesService.getAllStudentsEnrolledInCoursesAddedByProfessor(professorId);
     return ResponseEntity.ok(students);
 }
 @GetMapping("/withTasksandMilestones")
 public List<CourseDto> getCoursesWithTasksAndMilestones(@RequestParam("courseId") String courseId, @RequestParam("studentId") String studentId) {
     return coursesService.listCoursesWithTasksAndMilestonesForFriendsByCourseId(courseId,studentId);
 }

}

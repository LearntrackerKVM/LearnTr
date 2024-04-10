package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.CourseDetails;
import com.example.demo.data.StudentCourseRequest;
import com.example.demo.data.StudentCourses;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.service.StudentCoursesService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class StudentCoursesController {

	 private final StudentCoursesService studentcoursesService;
	 private final StudentCoursesRepository studentCoursesRepository;

	 @Autowired
	 public StudentCoursesController(StudentCoursesService studentcoursesService, StudentCoursesRepository studentCoursesRepository) {
	     this.studentcoursesService = studentcoursesService;
	     this.studentCoursesRepository = studentCoursesRepository;
	 }
	 @PostMapping("/addcourseforstudent")
	 public ResponseEntity<Object> saveStudentCourse(@RequestBody StudentCourseRequest request) {
	     // Check if the course already exists for the student
	     boolean courseExists = studentCoursesRepository.existsByStudentIdAndCourseId(
	             request.getStudentId(), request.getCourseId());

	     if (courseExists) {
	         // Return an error response if the course already exists
	         return ResponseEntity.badRequest().body("Error: Course already exists for this student.");
	     } else {
	         // Proceed with saving the new student course
	         StudentCourses savedStudentCourse = studentcoursesService.saveStudentCourse(
	                 request.getStudentId(), request.getCourseId(), request.getCourseCode());
	         return ResponseEntity.ok(savedStudentCourse);
	     }
	 }

	  @GetMapping("/courses")
	    public List<CourseDetails> getCoursesForStudent(@RequestParam String studentId) {
	        return studentcoursesService.getCourses(studentId);
	    }
	  
	  @GetMapping("/getNoOfStudentsEnrolled/{createdById}")
	    public ResponseEntity<Integer> getNoOfStudentsEnrolledByProfessor(@PathVariable String createdById) {
	        int numberOfStudentsEnrolled = studentcoursesService.countStudentsEnrolledByCreatedId(createdById);
	        return ResponseEntity.ok(numberOfStudentsEnrolled);
	    }
	  
	  @GetMapping("/getNoOfCoursesEnrolledbyStudent/{studentId}")
	    public ResponseEntity<Integer> getNoOfCoursesEnrolledByStudent(@PathVariable String studentId) {
	        int numberOfcoursesEnrolled = studentcoursesService.countCoursesEnrolledbyStudentId(studentId);
	        return ResponseEntity.ok(numberOfcoursesEnrolled);
	    }
}

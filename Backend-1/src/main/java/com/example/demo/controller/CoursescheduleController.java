package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.data.Courseschedule;

import com.example.demo.service.CoursescheduleService;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin("*")
public class CoursescheduleController {

	private final CoursescheduleService coursescheduleService;
	
	@Autowired
	public CoursescheduleController( CoursescheduleService coursescheduleService) {
		this.coursescheduleService = coursescheduleService;
	}
	 @PostMapping("/AddCourseschedule")
	 public ResponseEntity<Iterable<Courseschedule>> createCourseWithSchedules(@RequestBody List<Courseschedule> courseschedule) {
		 Iterable<Courseschedule> savedCourseschedule = coursescheduleService.AddCourseSchedule(courseschedule);
	     return ResponseEntity.ok(savedCourseschedule);
	 }
	 @GetMapping("/getSchedulebyCourses")
	 public ResponseEntity<List<Courseschedule>> getAllSchedulesByCourseIds(@RequestParam("courseIds") String courseIds) {
	     List<String> idList = Arrays.stream(courseIds.split(","))
	                                 .collect(Collectors.toList());
	     List<Courseschedule> schedules = coursescheduleService.getAllSchedulesByCourseIds(idList);
	     return ResponseEntity.ok(schedules);
	 }

}

package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Courseschedule;
import com.example.demo.repository.CourseScheduleRepository;


@Service
public class CoursescheduleService {

	private final CourseScheduleRepository courseScheduleRepository;
	
	
	 @Autowired
	 public CoursescheduleService(CourseScheduleRepository coursescheduleRepository) {

	     this.courseScheduleRepository = coursescheduleRepository;
	 }
	

public Iterable<Courseschedule> AddCourseSchedule(List<Courseschedule> courseschedule) {

	return courseScheduleRepository.saveAll(courseschedule);
}

public List<Courseschedule> getAllSchedulesByCourseIds(List<String> courseIds) {
    return courseScheduleRepository.findByCourseIdIn(courseIds);
}
}

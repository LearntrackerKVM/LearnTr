package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.Assignment;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, String> {
	
	 int countByCreatedById(String createdById);
	 List<Assignment> findByCourseId(String courseId);
	    Optional<Assignment> findByAssignmentNameAndCourseId(String assignmentName, String courseId);
}

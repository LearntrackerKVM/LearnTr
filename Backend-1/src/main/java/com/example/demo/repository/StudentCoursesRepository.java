package com.example.demo.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.StudentCourses;

@Repository
public interface StudentCoursesRepository extends CrudRepository<StudentCourses , String> {

	 List<StudentCourses> findCourseIdsByStudentId(String studentId);

	 int countByCourseIdIn(List<String> courseIds);
	 
	 int countByStudentId(String studentId);
	   List<StudentCourses> findByCourseId(String courseId);
	   
	   boolean existsByStudentIdAndCourseId(String studentId, String courseId);
	   List<StudentCourses> findByStudentId(String studentId);
	   List<StudentCourses> findByCourseIdIn(List<String> courseIds);

}

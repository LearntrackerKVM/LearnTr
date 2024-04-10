package com.example.demo.repository;

import com.example.demo.data.Courses;

import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesRepository extends CrudRepository<Courses, Integer> {

    List<Courses> findByCreatedByID(String createdByID);
    Courses findByCourseId(String courseId);
    @Query(value = "{ 'courseCode' : { $regex: ?0 } }", fields = "{ 'courseCode' : 1 }")
    List<Courses> findByCourseCodeStartingWith(String regex);
    
 
    long countByCreatedByID(String createdByID);
    List<String> findCourseIdsByCreatedByID(String createdById);
	List<Courses> findCoursesByCreatedByID(String createdById);
}

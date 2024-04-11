package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.StudentTasks;

@Repository
public interface StudentTasksRepository extends CrudRepository<StudentTasks, String> {

	List<StudentTasks> findByStudentId(String studentId);

	List<StudentTasks> findByCourseId(String courseId);

	List<StudentTasks> findByCourseIdAndStudentId(String courseId, String studentId);

}

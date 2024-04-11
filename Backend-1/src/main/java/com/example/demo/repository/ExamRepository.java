package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.Exam;

@Repository
public interface ExamRepository extends CrudRepository<Exam,String>{

	 int countByCreatedById(String createdById);
	 List<Exam> findByCourseId(String courseId);
}

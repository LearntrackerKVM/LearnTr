package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.Exam;

@Repository
public interface ExamRepository extends MongoRepository<Exam,String>{

	 int countByCreatedById(String createdById);
	 List<Exam> findByCourseId(String courseId);
}

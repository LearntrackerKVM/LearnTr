package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.data.Question;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findByStudentId(String studentId);

    List<Question> findByProfessorId(String professorId);
    List<Question> findByCourseId(String courseId);
}

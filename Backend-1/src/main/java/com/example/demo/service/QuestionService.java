package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Question;
import com.example.demo.repository.QuestionRepository;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question postQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question postAnswer(String questionId, String answer) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setAnswer(answer);
        return questionRepository.save(question);
    }

    public List<Question> getAllQuestionsByStudentId(String studentId) {
        return questionRepository.findByStudentId(studentId);
    }

    public List<Question> getAllQuestionsByProfessorId(String professorId) {
        return questionRepository.findByProfessorId(professorId);
    }
    public List<Question> getAllQuestionsByCourseId(String courseId) {
        return questionRepository.findByCourseId(courseId);
    }
    
}

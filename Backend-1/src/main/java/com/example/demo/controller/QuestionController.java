package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.data.Question;
import com.example.demo.service.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin("*")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/ask")
    public Question postQuestion(@RequestBody Question question) {
        return questionService.postQuestion(question);
    }

    @PostMapping(value = "/answer/{questionId}", consumes = "text/plain")
    public ResponseEntity<Question> postAnswer(@PathVariable String questionId, @RequestBody String answer) {
        Question question = questionService.postAnswer(questionId, answer);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/student/{studentId}")
    public List<Question> getAllQuestionsByStudentId(@PathVariable String studentId) {
        return questionService.getAllQuestionsByStudentId(studentId);
    }

    @GetMapping("/professor/{professorId}")
    public List<Question> getAllQuestionsByProfessorId(@PathVariable String professorId) {
        return questionService.getAllQuestionsByProfessorId(professorId);
    }
    @GetMapping("/course/{courseId}")
    public List<Question> getAllQuestionsByCourseId(@PathVariable String courseId) {
        return questionService.getAllQuestionsByCourseId(courseId);
    }
}

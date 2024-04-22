package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.data.Exam;
import com.example.demo.data.Exam;
import com.example.demo.service.ExamService;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin("*")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping
    public Iterable<Exam> getAllExams() {
        return examService.getAllExams();
    }

    @GetMapping("/{id}")
    public Exam getExamById(@PathVariable String id) {
        return examService.getExamById(id);
    }

    @PostMapping
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @DeleteMapping("/{id}")
    public void deleteExam(@PathVariable String id) {
        examService.deleteExam(id);
    }
    
    @GetMapping("/getNoOfExams/{createdById}")
    public ResponseEntity<Integer> getNoOfExamsAddedByProfessor(@PathVariable String createdById) {
        int numberOfCourses = examService.countExamsByCreatedId(createdById);
        return ResponseEntity.ok(numberOfCourses);
    }
    @GetMapping("/getExams/{courseId}")
    public ResponseEntity<List<Exam>> getExamsAddedByProfessor(@PathVariable String courseId) {
        List<Exam> numberOfExams = examService.getExamsByCourseId(courseId);
        return ResponseEntity.ok(numberOfExams);
    }
}

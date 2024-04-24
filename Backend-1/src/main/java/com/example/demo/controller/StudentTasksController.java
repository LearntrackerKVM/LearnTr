package com.example.demo.controller;

import com.example.demo.data.StudentTasks;
import com.example.demo.service.StudentTasksService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/studentTasks")
@CrossOrigin("*")
public class StudentTasksController {

    private final StudentTasksService studentTasksService;

    @Autowired
    public StudentTasksController(StudentTasksService studentTasksService) {
        this.studentTasksService = studentTasksService;
    }

    @GetMapping
    public ResponseEntity<Iterable<StudentTasks>> getAllStudentTasks() {
        Iterable<StudentTasks> tasks = studentTasksService.findAllStudentTasks();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<StudentTasks> createStudentTask(@RequestBody StudentTasks studentTask) {
        StudentTasks createdTask = studentTasksService.createOrUpdateStudentTask(studentTask);
        return ResponseEntity.ok(createdTask);
    }
    @GetMapping("/getNoOfAssignments/{studentId}")
    public ResponseEntity<Long> countDueAssignmentsForStudent(@PathVariable String studentId) {
        long numberOfCourses = studentTasksService.countDueAssignmentsForStudent(studentId);
        return ResponseEntity.ok(numberOfCourses);
    }
    
    @GetMapping("/getNoOfExams/{studentId}")
    public ResponseEntity<Long> countDueExamsForStudent(@PathVariable String studentId) {
        long numberOfCourses = studentTasksService.countDueExamsForStudent(studentId);
        return ResponseEntity.ok(numberOfCourses);
    }
    
    @GetMapping("/{studentId}")
    public List<StudentTasks> getAllTasksByStudentId(@PathVariable String studentId) {
        return studentTasksService.getAllTasksByStudentId(studentId);
    }
    
    
    @GetMapping("/{studentId}/{courseId}")
    public List<StudentTasks> getAllTasksByStudentIdandCourseId(@PathVariable String studentId,@PathVariable String courseId) {
        return studentTasksService.getAllTasksByStudentIdandCourseId(studentId,courseId);
    }
    

    @PutMapping("/{id}")
    public StudentTasks updateTask(@PathVariable String id,
                           @RequestParam String difficultyLevel,
                           @RequestParam String notes,
                           @RequestParam String status,
                           @RequestParam(required = false) LocalDate completionDate) {
        return studentTasksService.updateTask(id, difficultyLevel, notes, status, completionDate);
    }
    
    @PostMapping("/uploadFile/{studentTaskId}")
    public ResponseEntity<StudentTasks> uploadFileToStudentTask(
            @PathVariable String studentTaskId,
            @RequestPart("file") MultipartFile file) {
        return studentTasksService.updateTaskWithFile(studentTaskId, file)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        return studentTasksService.getFile(id)
                .map(task -> ResponseEntity.ok()
                        .header("Content-Type", task.getFileType())
                        .body(task.getFileContent()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

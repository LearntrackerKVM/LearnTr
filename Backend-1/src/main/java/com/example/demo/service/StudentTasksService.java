package com.example.demo.service;

import com.example.demo.data.StudentTasks;
import com.example.demo.repository.StudentTasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class StudentTasksService {

    private final StudentTasksRepository studentTasksRepository;

    @Autowired
    public StudentTasksService(StudentTasksRepository studentTasksRepository) {
        this.studentTasksRepository = studentTasksRepository;
    }

    public Iterable<StudentTasks> findAllStudentTasks() {
        return studentTasksRepository.findAll();
    }

    public Optional<StudentTasks> findStudentTaskById(String id) {
        return studentTasksRepository.findById(id);
    }

    public StudentTasks createOrUpdateStudentTask(StudentTasks studentTask) {
        return studentTasksRepository.save(studentTask);
    }

    public void deleteStudentTask(String id) {
        studentTasksRepository.deleteById(id);
    }
    public long countDueAssignmentsForStudent(String studentId) {
        Iterable<StudentTasks> tasks = findAllStudentTasks();
        LocalDate today = LocalDate.now();

        return StreamSupport.stream(tasks.spliterator(), false)
                .filter(task -> task.getStudentId().equals(studentId)) // Check if the task belongs to the specified student
                .filter(task -> "Assignment".equals(task.getTaskType())) // Include only assignments
                .filter(task -> {
                    if (task.getDueDate() == null) {
                        return false; // Ignore tasks without a due date
                    }
                    // Convert Date to LocalDate for comparison
                    LocalDate dueDate = task.getDueDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                    return dueDate.isAfter(today) || dueDate.isEqual(today); // Due today or in the future
                })
                .count();
    }
    public long countDueExamsForStudent(String studentId) {
        Iterable<StudentTasks> tasks = findAllStudentTasks();
        LocalDate today = LocalDate.now();

        return StreamSupport.stream(tasks.spliterator(), false)
                .filter(task -> task.getStudentId().equals(studentId)) // Check if the task belongs to the specified student
                .filter(task -> "Exam".equals(task.getTaskType())) // Include only assignments
                .filter(task -> {
                    if (task.getDueDate() == null) {
                        return false; // Ignore tasks without a due date
                    }
                    // Convert Date to LocalDate for comparison
                    LocalDate dueDate = task.getDueDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                    return dueDate.isAfter(today) || dueDate.isEqual(today); // Due today or in the future
                })
                .count();
    }

    public List<StudentTasks> getAllTasksByStudentId(String studentId) {
        return studentTasksRepository.findByStudentId(studentId);
    }

    
    public StudentTasks updateTask(String id, String difficultyLevel, String notes, String status, LocalDate completionDate) {
        return studentTasksRepository.findById(id).map(studentTask -> {
            if ("Completed".equals(status) && !"Completed".equals(studentTask.getStatus())) {
                studentTask.setStatus("Completed");
                // Convert LocalDate to java.sql.Date (which is a subclass of java.util.Date)
                studentTask.setCompletionDate(Date.valueOf(completionDate));
            } else if (!"Completed".equals(status)) {
                studentTask.setDifficultyLevel(difficultyLevel);
                studentTask.setNotes(notes);
            }
            // Save and return the updated task
            return studentTasksRepository.save(studentTask);
        }).orElseThrow(() -> new RuntimeException("Task not found with id " + id));
    }
    // Additional business logic and methods can be implemented as needed
}

package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Exam;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TasksMilestones;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;

@Service
public class ExamService {
   
    private ExamRepository examRepository;
    private final StudentCoursesRepository studentCoursesRepository;
    private final StudentTasksRepository studentTasksRepository;
    private final TaskMilestonesRepository taskMilestonesRepository;

    
    @Autowired
    public ExamService(ExamRepository examRepository, StudentCoursesRepository studentCoursesRepository, StudentTasksRepository studentTasksRepository, TaskMilestonesRepository taskMilestonesRepository) {
        this.examRepository = examRepository;
        this.studentCoursesRepository = studentCoursesRepository;
        this.studentTasksRepository = studentTasksRepository;
        this.taskMilestonesRepository = taskMilestonesRepository;
    }

    public Iterable<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(String id) {
        return examRepository.findById(id).orElse(null);
    }

    public Exam createExam(Exam exam) {
        // Step 1: Save the Exam
        Exam savedExam = examRepository.save(exam);

        // Step 2: Fetch students enrolled in the course
        List<StudentCourses> enrollments = studentCoursesRepository.findByCourseId(exam.getCourseId());

        // Step 3: Iterate over each enrolled student and create a StudentTask
        for (StudentCourses enrollment : enrollments) {
            StudentTasks studentTask = new StudentTasks();
            studentTask.setStudentId(enrollment.getStudentId());
            studentTask.setTaskId(savedExam.getId());
            studentTask.setCourseId(exam.getCourseId());
            studentTask.setStatus("Not Started");
            studentTask.setTitle(exam.getExamName());
            studentTask.setTaskType("Exam");
            studentTask.setCourseName(exam.getCourseName());
            LocalDate localDate = exam.getExamDate();
            Date date = Date.valueOf(localDate); // Convert LocalDate to java.sql.Date
            studentTask.setDueDate(date); 
            studentTask.setMilestones(exam.getSyllabus().length);
            StudentTasks savedStudentTask = studentTasksRepository.save(studentTask);

            // Step 4: Create TaskMilestones for each syllabus item
            int sequence = 1;
            for (String syllabusItem : exam.getSyllabus()) {
                TasksMilestones milestone = new TasksMilestones();
                milestone.setStudentTaskId(savedStudentTask.getId());
                milestone.setTitle("Syllabus Item " + sequence);
                milestone.setDescription(syllabusItem);
                milestone.setStatus("Not Started");
                milestone.setStudentTaskId(savedStudentTask.getId());
                milestone.setStudentId(savedStudentTask.getStudentId());
                milestone.setIsComplete(false);

                milestone.setSequenceNumber(sequence++);
                taskMilestonesRepository.save(milestone);
            }
        }

        return savedExam;
    }

    public void deleteExam(String id) {
        examRepository.deleteById(id);
    }
    
    public int countExamsByCreatedId(String createdById) {
        return examRepository.countByCreatedById(createdById);
    }
}

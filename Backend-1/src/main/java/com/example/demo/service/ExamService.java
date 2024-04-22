package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Assignment;
import com.example.demo.data.Exam;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TasksMilestones;
import com.example.demo.data.User;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ExamService {
   
    private ExamRepository examRepository;
    private final StudentCoursesRepository studentCoursesRepository;
    private final StudentTasksRepository studentTasksRepository;
    private final TaskMilestonesRepository taskMilestonesRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    
    @Autowired
    public ExamService(ExamRepository examRepository, StudentCoursesRepository studentCoursesRepository, StudentTasksRepository studentTasksRepository, TaskMilestonesRepository taskMilestonesRepository,
    		 UserRepository userRepository, EmailService emailService) {
        this.examRepository = examRepository;
        this.studentCoursesRepository = studentCoursesRepository;
        this.studentTasksRepository = studentTasksRepository;
        this.taskMilestonesRepository = taskMilestonesRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public Iterable<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExamById(String id) {
        return examRepository.findById(id).orElse(null);
    }

    public Exam createExam(Exam exam) {
        Exam savedExam = examRepository.save(exam);

        List<StudentCourses> enrollments = studentCoursesRepository.findByCourseId(exam.getCourseId());
        enrollments.forEach(enrollment -> {
            Optional<User> optStudent = userRepository.findById(enrollment.getStudentId());
            optStudent.ifPresent(student -> sendExamNotification(savedExam, student));

            createOrUpdateStudentTask(savedExam, enrollment);
        });

        return savedExam;
    }

    private void sendExamNotification(Exam exam, User student) {
        String notificationMessage = String.format(
            "New Exam posted for: %s\n" +
            "By professor: %s\n" +
            "Exam: %s\n" +
            "Due date: %s\n" +
            "Exam Time: %s\n" +
            "Exam Room: %s\n",
            exam.getCourseName(), 
            exam.getCreatedBy(), 
            exam.getExamName(), 
            exam.getExamDate(),
            exam.getExamTime(),
            exam.getRoomNumber()
        );
        emailService.sendSimpleMessage(student.getEmail(), "New Exam Notification", notificationMessage);
    }

    public void createOrUpdateStudentTask(Exam exam, StudentCourses enrollment) {
    	  StudentTasks studentTask = new StudentTasks();
          studentTask.setStudentId(enrollment.getStudentId());
          studentTask.setTaskId(exam.getId());
          studentTask.setCourseId(exam.getCourseId());
          studentTask.setStatus("Not Started");
          studentTask.setTitle(exam.getExamName());
          studentTask.setTaskType("Exam");
          studentTask.setCourseName(exam.getCourseName());
          LocalDate localDate = exam.getExamDate();
          Date date = Date.valueOf(localDate); // Convert LocalDate to java.sql.Date
          studentTask.setDueDate(date); 
          studentTask.setMilestonesCompleted(0);
          studentTask.setMilestones(exam.getSyllabus().length);
          StudentTasks savedStudentTask = studentTasksRepository.save(studentTask);
          createOrUpdateMilestones(exam,savedStudentTask);
    }

    private void createOrUpdateMilestones(Exam exam, StudentTasks studentTask) {
    
    	  int sequence = 1;
          for (String syllabusItem : exam.getSyllabus()) {
              TasksMilestones milestone = new TasksMilestones();
              milestone.setStudentTaskId(studentTask.getId());
              milestone.setTitle("Syllabus Item " + sequence);
              milestone.setDescription(syllabusItem);
              milestone.setStatus("Not Started");
              milestone.setStudentTaskId(studentTask.getId());
              milestone.setStudentId(studentTask.getStudentId());
              milestone.setIsComplete(false);

              milestone.setSequenceNumber(sequence++);
              taskMilestonesRepository.save(milestone);
          }
      }


    public void deleteExam(String id) {
        examRepository.deleteById(id);
    }
    
    public int countExamsByCreatedId(String createdById) {
        return examRepository.countByCreatedById(createdById);
    }
    
    public List<Exam> getExamsByCourseId(String courseId) {
        return examRepository.findByCourseId(courseId);
    }
    
}

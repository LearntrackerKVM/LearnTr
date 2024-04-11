package com.example.demo.service;

import java.io.IOException;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Assignment;
import com.example.demo.data.Exam;
import com.example.demo.data.CourseDetails;
import com.example.demo.data.Courses;
import com.example.demo.data.Courseschedule;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.StudentTasks;
import com.example.demo.repository.AssignmentRepository;
import com.example.demo.repository.CourseScheduleRepository;
import com.example.demo.repository.CoursesRepository;
import com.example.demo.repository.ExamRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;

@Service
public class StudentCoursesService {

	

    private final StudentCoursesRepository studentCoursesRepository;
    private final CoursesRepository coursesRepository;
    private final CourseScheduleRepository courseScheduleRepository;
  
    private final AssignmentRepository assignmentRepository;


    private final ExamRepository examRepository;

    private final StudentTasksRepository studentTasksRepository;
    private final TaskMilestonesRepository taskMilestonesRepository;

    @Autowired
    public StudentCoursesService(StudentCoursesRepository studentCoursesRepository, CoursesRepository courseRepository, CourseScheduleRepository courseScheduleRepository, ExamRepository examRepository, AssignmentRepository assignmentRepository,StudentTasksRepository studentTasksRepository, TaskMilestonesRepository taskMilestonesRepository) {
        this.studentCoursesRepository = studentCoursesRepository;
        this.coursesRepository = courseRepository;
        this.courseScheduleRepository = courseScheduleRepository;
        this.examRepository = examRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentTasksRepository = studentTasksRepository;
        this.taskMilestonesRepository = taskMilestonesRepository;
    }


    public StudentCourses saveStudentCourse(String studentId, String courseId, String courseCode) {
        StudentCourses studentCourses = new StudentCourses();
        studentCourses.setStudentId(studentId);
        studentCourses.setCourseId(courseId);
        Courses savcourse = coursesRepository.findByCourseId(courseId);
        studentCourses.setCourseName(savcourse.getCourseName());
        studentCourses.setCourseCode(courseCode);
        // Set the enrolled date and created by fields as necessary
        
        StudentCourses savedStudentCourses = studentCoursesRepository.save(studentCourses);

        Courses course = coursesRepository.findByCourseId(courseId);
        if (course != null) {
            int updatedAvailableSlots = course.getAvailableSlots() - 1;
            if (updatedAvailableSlots >= 0) {
                course.setAvailableSlots(updatedAvailableSlots);
                coursesRepository.save(course);
                
            } else {
                throw new IllegalStateException("Available slots cannot be negative");
            }
        } else {
            throw new IllegalArgumentException("Course not found with ID: " + courseId);
        }
//        addTasksToStudent(studentId, courseId);

        return savedStudentCourses;
    }
//    
//    private void addTasksToStudent(String studentId, String courseId) {
//        // Handle assignments
//        List<Assignment> assignments = assignmentRepository.findByCourseId(courseId);
//        assignments.forEach(assignment -> createStudentTaskFromAssignment(studentId, assignment));
//
//        // Handle exams
//        List<Exam> exams = examRepository.findByCourseId(courseId);
//        exams.forEach(exam -> createStudentTaskFromExam(studentId, exam));
//    }


public List<CourseDetails> getCourses(String studentId) {
    List<CourseDetails> courseDetailsList = new ArrayList<>();
    List<StudentCourses> courseIds = studentCoursesRepository.findCourseIdsByStudentId(studentId);
    for (StudentCourses courseId : courseIds) {
        Courses course = coursesRepository.findByCourseId(courseId.getCourseId());
        if (course != null) {
            // Fetch course schedule details for the course
            List<Courseschedule> courseSchedules = courseScheduleRepository.findByCourseId(courseId.getCourseId());

            // Construct CourseDetails object combining course and course schedule details
            CourseDetails courseDetails = new CourseDetails();
            courseDetails.setCourseId(courseId.getCourseId());
            courseDetails.setCourseName(course.getCourseName());
            courseDetails.setCourseCode(course.getCourseCode());
            courseDetails.setAvailableSlots(course.getAvailableSlots());
            courseDetails.setCourseSchedules(courseSchedules);

            // Add CourseDetails to the list
            courseDetailsList.add(courseDetails);
        }
    }

    return courseDetailsList;
}
public int countStudentsEnrolledByCreatedId(String createdById) {
    List<Courses> courses = coursesRepository.findCoursesByCreatedByID(createdById);
    if (courses.isEmpty()) {
        return 0; // No courses found for the given createdById
    }
    
    List<String> courseIds = courses.stream()
                                    .map(Courses::getCourseId) // Assuming getId returns the course ID
                                    .collect(Collectors.toList());
    
    return studentCoursesRepository.countByCourseIdIn(courseIds);
}

public int countCoursesEnrolledbyStudentId(String studentId) 
{
    
    return studentCoursesRepository.countByStudentId(studentId);
}
}
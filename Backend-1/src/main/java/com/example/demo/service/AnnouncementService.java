package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.data.Announcement;
import com.example.demo.data.CourseVisibility;
import com.example.demo.data.StudentCourses;
import com.example.demo.data.User;
import com.example.demo.repository.AnnouncementRepository;
import com.example.demo.repository.StudentCoursesRepository;
import com.example.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    
    @Autowired
    private  SimpMessagingTemplate messagingTemplate;
    
    private final StudentCoursesRepository studentCoursesRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CoursesService courseService;
    
    
    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository, StudentCoursesRepository studentCoursesRepository,UserRepository userRepository,
    		EmailService emailService, CoursesService coursesService) {
        this.announcementRepository = announcementRepository;
        this.studentCoursesRepository = studentCoursesRepository;
        this.userRepository = userRepository;
        this.emailService  = emailService;
        this.courseService = coursesService;
    }

    public Iterable<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(String id) {
        return announcementRepository.findById(id);
    }

    public Announcement createAnnouncement(Announcement announcement) {
        Announcement savedAnnouncement = announcementRepository.save(announcement);
        notifyRelevantStudents(savedAnnouncement);
        return savedAnnouncement;
    }

    private void notifyRelevantStudents(Announcement announcement) {
        List<String> courseIds = announcement.getVisibility().stream()
                                             .map(CourseVisibility::getCourseId)
                                             .collect(Collectors.toList());

        // Find all students enrolled in these courses
        List<StudentCourses> enrolledStudents = studentCoursesRepository.findByCourseIdIn(courseIds);

        String assignmentName = announcement.getTitle(); // Assuming title of the announcement is the assignment name
        String dueDate = announcement.getCreatedDate().toString(); // Formatting date might be necessary

        for (StudentCourses sc : enrolledStudents) {
            Optional<User> optStudent = userRepository.findById(sc.getStudentId());
            if (optStudent.isPresent()) {
                User student = optStudent.get();
                String visibilityDetails = announcement.getVisibility().stream()
                                                       .filter(v -> v.getCourseId().equals(sc.getCourseId()))
                                                       .map(CourseVisibility::getCourseName)
                                                       .findFirst()
                                                       .orElse("Restricted"); // Example processing of visibility

                String priority = announcement.getPriorityName();
                String professorName = announcement.getCreatedBy();

                // Prepare the notification message
                String notificationMessage = String.format(
                	    "New Announcement posted for: %s\n" + // Course visibility details
                	    "By professor: %s\n" + // Professor's name
                	    "Announcement: %s\n" + // Assignment name
                	    "Created date: %s\n" + // Due date
                	    "Priority: %s.", // Priority level
                	    visibilityDetails, 
                	    professorName, 
                	    assignmentName, 
                	    dueDate, 
                	    priority
                	);


                // Send the email with priority in the subject
                emailService.sendSimpleMessage(student.getEmail(), "New Announcement Notification - Priority: " + priority, notificationMessage);
            }
        }
    }



    public Announcement updateAnnouncement(String id, Announcement announcement) {
        if (announcementRepository.existsById(id)) {
            announcement.setId(id);
            return announcementRepository.save(announcement);
        } else {
            throw new IllegalArgumentException("Announcement with ID " + id + " does not exist.");
        }
    }

    public void deleteAnnouncementById(String id) {
        announcementRepository.deleteById(id);
    }
    public List<Announcement> getAnnouncementsByCourseId(String courseId) {
        return announcementRepository.findByVisibilityCourseId(courseId);
    }
}

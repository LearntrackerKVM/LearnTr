package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.data.Announcement;
import com.example.demo.service.AnnouncementService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
@CrossOrigin("*")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public Iterable<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> getAnnouncementById(@PathVariable String id) {
        Optional<Announcement> announcement = announcementService.getAnnouncementById(id);
        return announcement.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        Announcement createdAnnouncement = announcementService.createAnnouncement(announcement);
        return new ResponseEntity<>(createdAnnouncement, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable String id, @RequestBody Announcement announcement) {
        Announcement updatedAnnouncement = announcementService.updateAnnouncement(id, announcement);
        return new ResponseEntity<>(updatedAnnouncement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnouncementById(@PathVariable String id) {
        announcementService.deleteAnnouncementById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/byCourseId/{courseId}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByCourseId(@PathVariable String courseId) {
        List<Announcement> announcements = announcementService.getAnnouncementsByCourseId(courseId);
        return new ResponseEntity<>(announcements, HttpStatus.OK);
    }
}

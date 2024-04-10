package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Announcement;
import com.example.demo.repository.AnnouncementRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    @Autowired
    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public Iterable<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(String id) {
        return announcementRepository.findById(id);
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
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

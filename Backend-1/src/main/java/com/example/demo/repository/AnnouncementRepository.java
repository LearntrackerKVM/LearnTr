package com.example.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.Announcement;

@Repository
public interface AnnouncementRepository extends CrudRepository<Announcement, String> {
    // Additional custom query methods can be added here if needed
	 List<Announcement> findByVisibilityCourseId(String courseId);
}
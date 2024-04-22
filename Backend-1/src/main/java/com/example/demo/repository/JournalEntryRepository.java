package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.data.JournalEntry;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, String> {
    List<JournalEntry> findByStudentId(String studentId);
    @Query("{'studentId': ?0, 'date': {$gte: ?1, $lt: ?2}}")
    JournalEntry findByStudentIdAndDateRange(String studentId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
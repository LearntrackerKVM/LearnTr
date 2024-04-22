package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.data.Habit;

public interface HabitRepository extends MongoRepository<Habit, String> {
    List<Habit> findByStudentId(String studentId);
}
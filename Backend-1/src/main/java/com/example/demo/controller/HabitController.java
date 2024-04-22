package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.Habit;
import com.example.demo.service.HabitService;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin("*")
public class HabitController {

    @Autowired
    private HabitService habitService;

    // Create a new habit
    @PostMapping
    public ResponseEntity<Habit> createHabit(@RequestBody Habit habit) {
        Habit createdHabit = habitService.createHabit(habit);
        return new ResponseEntity<>(createdHabit, HttpStatus.CREATED);
    }

    // Delete a habit by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(@PathVariable String id) {
        habitService.deleteHabit(id);
        return ResponseEntity.ok().build();
    }

    // Edit an existing habit
    @PutMapping("/{id}")
    public ResponseEntity<Habit> editHabit(@PathVariable String id, @RequestBody Habit updatedHabit) {
        Optional<Habit> habit = habitService.editHabit(id, updatedHabit);
        if (habit.isPresent()) {
            return new ResponseEntity<>(habit.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all habits
    @GetMapping
    public ResponseEntity<List<Habit>> getAllHabits() {
        List<Habit> habits = habitService.getAllHabits();
        return new ResponseEntity<>(habits, HttpStatus.OK);
    }

    // Get a single habit by ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Habit>> getHabitsByStudentId(@PathVariable String studentId) {
        List<Habit> habits = habitService.getHabitsByStudentId(studentId);
        if (habits.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(habits, HttpStatus.OK);
        }
    }

}
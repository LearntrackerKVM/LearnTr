package com.example.demo.service;



import com.example.demo.data.Habit;
import com.example.demo.repository.HabitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    // Create a new habit
    public Habit createHabit(Habit habit) {
        return habitRepository.save(habit);
    }

    // Delete a habit by ID
    public void deleteHabit(String id) {
        habitRepository.deleteById(id);
    }

    // Edit an existing habit
    public Optional<Habit> editHabit(String id, Habit updatedHabit) {
        return habitRepository.findById(id)
            .map(habit -> {
                habit.setName(updatedHabit.getName());
                habit.setCompleted(updatedHabit.completed());
                return habitRepository.save(habit);
            });
    }

    // Get all habits
    public List<Habit> getAllHabits() {
        return habitRepository.findAll();
    }

    // Get all habits for a specific student
    public List<Habit> getHabitsByStudentId(String studentId) {
        return habitRepository.findByStudentId(studentId);
    }
}

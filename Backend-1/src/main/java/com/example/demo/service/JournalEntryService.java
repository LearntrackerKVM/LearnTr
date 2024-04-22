package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Habit;
import com.example.demo.data.JournalEntry;
import com.example.demo.repository.JournalEntryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    // Save a new journal entry
    public JournalEntry saveJournalEntry(JournalEntry entry) {
        return journalEntryRepository.save(entry);
    }

    // Get all journal entries by student ID
    public List<JournalEntry> getJournalEntriesByStudentId(String studentId) {
        return journalEntryRepository.findByStudentId(studentId);
    }

    // Delete a journal entry
    public void deleteJournalEntry(String id) {
        journalEntryRepository.deleteById(id);
    }

 

    public JournalEntry updateJournalEntry(String id, JournalEntry updatedEntry) {
        return journalEntryRepository.findById(id)
            .map(entry -> {
                entry.setMorningMood(updatedEntry.getMorningMood());
                entry.setNightMood(updatedEntry.getNightMood());
                entry.setTasks(updatedEntry.getTasks());
                entry.setPriorities(updatedEntry.getPriorities());
                entry.setNotes(updatedEntry.getNotes());
                entry.setImportantDay(updatedEntry.importantDay());
                entry.setImportantDayDescription(updatedEntry.getImportantDayDescription());
                entry.setColorForTheDay(updatedEntry.getColorForTheDay());
                entry.setNightDiary(updatedEntry.getNightDiary());

                // Update habits if not null and only if they exist
                if (updatedEntry.getHabits() != null) {
                    updateHabits(entry.getHabits(), updatedEntry.getHabits());
                }

                // Save the updated entry back to the database
                return journalEntryRepository.save(entry);
            })
            .orElseGet(() -> {
                // Entry not found, handle appropriately
                throw new IllegalStateException("Journal entry not found with id: " + id);
            });
    }

    private void updateHabits(List<Habit> existingHabits, List<Habit> updatedHabits) {
        // This is a simple replacement strategy; for more complex scenarios, you might need to merge lists based on IDs
        existingHabits.clear(); // Clear existing habits
        existingHabits.addAll(updatedHabits); // Add all from updated habits
    }
    public JournalEntry getJournalEntryByStudentIdAndDate(String studentId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // Midnight at the start of the day
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // Just before midnight at the end of the day

        System.out.println("Fetching journal entry for studentId: " + studentId + " on date: " + date);
        JournalEntry entry = journalEntryRepository.findByStudentIdAndDateRange(studentId, startOfDay, endOfDay);
    
        return entry;
    }

}

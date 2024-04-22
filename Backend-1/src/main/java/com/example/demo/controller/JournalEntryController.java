package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.data.JournalEntry;
import com.example.demo.service.JournalEntryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/journal")
@CrossOrigin("*")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @PostMapping
    public ResponseEntity<JournalEntry> createJournalEntry(@RequestBody JournalEntry entry) {
        // Assuming entry.date is already in LocalDate format or convert it
        LocalDate localDate = entry.getDate().atStartOfDay().toLocalDate(); // Extracts just the date part
        entry.setDate(localDate);

        JournalEntry newEntry = journalEntryService.saveJournalEntry(entry);
        return ResponseEntity.ok(newEntry);
    }

    // Get journal entries by student ID
    @GetMapping("/{studentId}")
    public ResponseEntity<List<JournalEntry>> getJournalEntriesByStudentId(@PathVariable String studentId) {
        List<JournalEntry> entries = journalEntryService.getJournalEntriesByStudentId(studentId);
        return ResponseEntity.ok(entries);
    }

    // Delete a journal entry
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJournalEntry(@PathVariable String id) {
        journalEntryService.deleteJournalEntry(id);
        return ResponseEntity.ok().build();
    }

    // Update a journal entry
    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateJournalEntry(@PathVariable String id, @RequestBody JournalEntry entry) {
        JournalEntry updatedEntry = journalEntryService.updateJournalEntry(id, entry);
        return ResponseEntity.ok(updatedEntry);
    }
    
    
    @GetMapping("/{studentId}/{date}")
    public ResponseEntity<?> getEntry(
        @PathVariable String studentId, 
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        JournalEntry entry = journalEntryService.getJournalEntryByStudentIdAndDate(studentId, date);
        return ResponseEntity.ok(entry);
    }
}

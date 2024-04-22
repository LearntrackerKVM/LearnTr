package com.example.demo.controller;

import com.example.demo.data.Notebook;
import com.example.demo.service.NotebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notebooks")
@CrossOrigin("*")
public class NotebookController {

    @Autowired
    private NotebookService notebookService;

    @PostMapping
    public ResponseEntity<Notebook> createNotebook(@RequestBody Notebook notebook) {
        return ResponseEntity.ok(notebookService.saveNotebook(notebook));
    }

    @GetMapping
    public ResponseEntity<List<Notebook>> getAllNotebooks() {
        return ResponseEntity.ok(notebookService.findAllNotebooks());
    }

    @GetMapping("getnotebookbyStudentId/{studentId}")
    public ResponseEntity<Notebook> getNotebookByStudentId(@PathVariable String studentId) {
        Optional<Notebook> notebook = notebookService.findNotebookBystudentId(studentId);
        return notebook.isPresent() ? ResponseEntity.ok(notebook.get()) : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Notebook> getNotebookById(@PathVariable String id) {
        Optional<Notebook> notebook = notebookService.findNotebookById(id);
        return notebook.isPresent() ? ResponseEntity.ok(notebook.get()) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notebook> updateNotebook(@PathVariable String id, @RequestBody Notebook updatedNotebook) {
        Optional<Notebook> oldNotebook = notebookService.findNotebookById(id);
        if (oldNotebook.isPresent()) {
            updatedNotebook.setId(id);
            return ResponseEntity.ok(notebookService.saveNotebook(updatedNotebook));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotebook(@PathVariable String id) {
        if (notebookService.findNotebookById(id).isPresent()) {
            notebookService.deleteNotebook(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}

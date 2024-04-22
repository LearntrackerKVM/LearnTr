package com.example.demo.service;

import com.example.demo.data.Notebook;
import com.example.demo.repository.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    public Notebook saveNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public List<Notebook> findAllNotebooks() {
        return notebookRepository.findAll();
    }

    public Optional<Notebook> findNotebookById(String id) {
        return notebookRepository.findById(id);
    }

    public Optional<Notebook> findNotebookBystudentId(String studentId){
    	return notebookRepository.findByStudentId(studentId);
    }
    public void deleteNotebook(String id) {
        notebookRepository.deleteById(id);
    }
}
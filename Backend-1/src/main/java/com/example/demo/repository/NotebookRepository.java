package com.example.demo.repository;

import com.example.demo.data.Notebook;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends MongoRepository<Notebook, String> {
	
    Optional<Notebook> findByStudentId(String studentId);
}
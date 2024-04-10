package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.data.Discussion;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiscussionRepository extends MongoRepository<Discussion, String> { 
    
	List<Discussion> findByCourseID(String courseID);
}

package com.example.demo.repository;

import com.example.demo.data.StudentFriends;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFriendsRepository extends MongoRepository<StudentFriends, String> {
	
	  List<StudentFriends> findByStudentId(String studentId);
	   Optional<StudentFriends> findFirstByStudentId(String studentId);
Set<String> findFriendIdsByStudentId(String studentId);



	  
}

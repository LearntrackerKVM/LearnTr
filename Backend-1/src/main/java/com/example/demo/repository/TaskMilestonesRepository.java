package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.data.TasksMilestones;

@Repository
public interface TaskMilestonesRepository extends MongoRepository<TasksMilestones, String>{
	 @Query(value = "{'studentTaskId': {$in: ?0}}", count = true)
	    int countByStudentTaskIds(List<String> studentTaskIds); 
	   List<TasksMilestones> findByStudentTaskId(String studentTaskId);
	int countByStudentIdAndIsComplete(String userId, boolean b);
	 List<TasksMilestones> findByStudentTaskIdAndTitle(String studentTaskId, String title);
	List<TasksMilestones> findByStudentTaskIdAndStudentId(String id, String studentId);
}

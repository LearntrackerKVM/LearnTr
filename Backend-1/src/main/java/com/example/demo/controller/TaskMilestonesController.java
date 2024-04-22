package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.MilestoneUserInfo;
import com.example.demo.data.TasksMilestones;
import com.example.demo.service.TaskMilestonesService;
@RestController
@RequestMapping("/api/tasksMilestones")
@CrossOrigin("*")
public class TaskMilestonesController {

	private final TaskMilestonesService taskMilestonesService;
	  @Autowired
	    public TaskMilestonesController(TaskMilestonesService taskMilestonesService) {
	        this.taskMilestonesService = taskMilestonesService;
	    }
	   @GetMapping("/getNoOfMilestones/{studentId}")
	    public ResponseEntity<Long> getNoOfMilestonesForStudent(@PathVariable String studentId) {
	        long numberOfCourses = taskMilestonesService.countCompletedMilestonesForStudent(studentId);
	        return ResponseEntity.ok(numberOfCourses);
	    }
	   
	   @GetMapping("/getmilestones/{studentTaskId}")
	    public List<TasksMilestones> getMilestonesByStudentTaskId(@PathVariable String studentTaskId) {
	        return taskMilestonesService.getMilestonesByStudentTaskId(studentTaskId);
	    }
	   
	   @PutMapping("/updateMilestone/{milestoneId}")
	   public ResponseEntity<TasksMilestones> updateMilestone(
	           @PathVariable String milestoneId, 
	           @RequestBody TasksMilestones updatedMilestone) {
	       
	       TasksMilestones milestone = taskMilestonesService.updateMilestone(milestoneId, updatedMilestone);
	       if (milestone == null) {
	           return ResponseEntity.notFound().build();
	       }
	       return ResponseEntity.ok(milestone);
	   }
	   
	   @GetMapping("/getMilestonesWithUserInfo/{studentTaskId}/{title}")
	   public ResponseEntity<List<MilestoneUserInfo>> getMilestonesWithUserInfo(
	           @PathVariable String studentTaskId, @PathVariable String title) {
	       
	       List<MilestoneUserInfo> milestoneUserInfos = taskMilestonesService.getMilestonesWithUserInfo(studentTaskId, title);
	       
	       if (milestoneUserInfos.isEmpty()) {
	           return ResponseEntity.notFound().build();
	       }
	       
	       return ResponseEntity.ok(milestoneUserInfos);
	   }
	   

}

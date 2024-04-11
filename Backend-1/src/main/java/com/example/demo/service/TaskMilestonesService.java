package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.MilestoneUserInfo;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TasksMilestones;
import com.example.demo.data.User;
import com.example.demo.repository.StudentTasksRepository;
import com.example.demo.repository.TaskMilestonesRepository;
import com.example.demo.repository.UserRepository;
@Service
public class TaskMilestonesService {
	 @Autowired
	    private StudentTasksRepository studentTasksRepository;

	    @Autowired
	    private TaskMilestonesRepository taskMilestonesRepository;
	    
	    @Autowired
	    private UserService userService;
	    @Autowired
	    private UserRepository userRepository;

	    public int countMilestonesForStudent(String studentId) {
	        // Retrieve all StudentTasks for the given studentId
	        List<StudentTasks> studentTasks = studentTasksRepository.findByStudentId(studentId);

	        // Extract the IDs of these StudentTasks
	        List<String> studentTaskIds = studentTasks.stream()
	                .map(StudentTasks::getId)
	                .collect(Collectors.toList());

	        // Count milestones for these studentTaskIds
	        return taskMilestonesRepository.countByStudentTaskIds(studentTaskIds);
	    }
	    
	    public List<TasksMilestones> getMilestonesByStudentTaskId(String studentTaskId) {
	        return taskMilestonesRepository.findByStudentTaskId(studentTaskId);
	    }
	    public TasksMilestones updateMilestone(String milestoneId, TasksMilestones updatedMilestone) {
	        Optional<TasksMilestones> existingMilestoneOpt = taskMilestonesRepository.findById(milestoneId);

	        if (existingMilestoneOpt.isPresent()) {
	            TasksMilestones existingMilestone = existingMilestoneOpt.get();

	            // Update its properties
	            existingMilestone.setDifficultyLevel(updatedMilestone.getDifficultyLevel());
	            existingMilestone.setNotes(updatedMilestone.getNotes());
	            existingMilestone.setIsComplete(updatedMilestone.getIsComplete());
	            // Add other fields as necessary

	            if (Boolean.TRUE.equals(updatedMilestone.getIsComplete())) {
	                existingMilestone.setCompletionDate(new Date()); // Set to current date
	                taskMilestonesRepository.save(existingMilestone);
	                
	                // After saving the milestone, check if all milestones for the task are completed
	                checkAndCompleteTask(existingMilestone.getStudentTaskId());
	                
	                // Update the user's badge based on completed milestones
	                updateUserBadge(existingMilestone.getStudentId()); // Ensure you have a way to get the userId from the milestone
	            }

	            // Save the updated milestone back to the database
	            TasksMilestones savedMilestone = taskMilestonesRepository.save(existingMilestone);

	            // After saving the milestone, check if all milestones for the task are completed
	            checkAndCompleteTask(existingMilestone.getStudentTaskId());

	            return savedMilestone;
	        } else {
	            // Milestone not found
	            throw new RuntimeException("Milestone not found with id: " + milestoneId);
	        }
	    }

	    private void checkAndCompleteTask(String studentTaskId) {
	        List<TasksMilestones> milestones = taskMilestonesRepository.findByStudentTaskId(studentTaskId);
	        
	        boolean allCompleted = milestones.stream().allMatch(TasksMilestones::getIsComplete);

	        if (allCompleted) {
	            // Fetch the task
	            Optional<StudentTasks> taskOpt = studentTasksRepository.findById(studentTaskId);
	            if (taskOpt.isPresent()) {
	                StudentTasks task = taskOpt.get();
	                task.setStatus("Completed");
	                task.setCompletionDate(new Date()); // Set completion to current date
	                studentTasksRepository.save(task);
	            } else {
	                throw new RuntimeException("Task not found with id: " + studentTaskId);
	            }
	        }
	    }
	    private String determineBadge(int completedMilestones) {
	        if (completedMilestones <= 5) return "Bronze";
	        else if (completedMilestones <= 10) return "Silver";
	        else if (completedMilestones <= 15) return "Gold";
	        else if (completedMilestones <= 20) return "Platinum";
	        else if (completedMilestones <= 25) return "Diamond";
	        else if (completedMilestones <= 30) return "Ruby";
	        else if (completedMilestones <= 35) return "Sapphire";
	        else return "Emerald"; // Assumes more than 35 completed milestones leads to Emerald
	    }
	    private void updateUserBadge(String userId) {
	        // Count the completed milestones for the user
	        int completedMilestonesCount = taskMilestonesRepository.countByStudentIdAndIsComplete(userId, true);

	        // Determine the new badge based on the count
	        String newBadge = determineBadge(completedMilestonesCount);

	        // Update the user's record with the new badge and the count of completed milestones
	        // This assumes your userRepository has a method like updateUserBadgeAndCount that takes the userId, newBadge, and count
	        userService.updateBadgeAndMilestoneCount(userId, newBadge, completedMilestonesCount);
	    }

	    public List<MilestoneUserInfo> getMilestonesWithUserInfo(String studentTaskId, String title) {
	        List<TasksMilestones> milestones = taskMilestonesRepository.findByStudentTaskIdAndTitle(studentTaskId, title);
	        
	        return milestones.stream().map(milestone -> {
	            Optional<User> userOptional = userRepository.findById(milestone.getStudentId());
	            if(userOptional.isPresent()) {
	                User user = userOptional.get();
	                return new MilestoneUserInfo(milestone, user);
	            } else {
	                return null; // Or handle missing user differently
	            }
	        }).collect(Collectors.toList());
	    }

	}
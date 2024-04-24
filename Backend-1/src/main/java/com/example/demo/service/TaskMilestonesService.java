package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.Friend;
import com.example.demo.data.MilestoneUserInfo;
import com.example.demo.data.StudentFriends;
import com.example.demo.data.StudentTasks;
import com.example.demo.data.TasksMilestones;
import com.example.demo.data.User;
import com.example.demo.repository.StudentFriendsRepository;
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
	    @Autowired
	    private EmailService emailService;
	    
	    @Autowired
	    private StudentFriendsRepository studentFriendsRepository; 
	    public int countCompletedMilestonesForStudent(String studentId) {
	        // Fetch all StudentTasks for the given studentId
	        List<StudentTasks> studentTasks = studentTasksRepository.findByStudentId(studentId);

	        // Sum the milestonesCompleted for each task
	        int totalCompletedMilestones = studentTasks.stream()
	                                                   .mapToInt(StudentTasks::getMilestonesCompleted) // Extract completed milestones
	                                                   .sum(); // Sum up all completed milestones

	        return totalCompletedMilestones;
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

	            if (Boolean.TRUE.equals(updatedMilestone.getIsComplete()) && existingMilestone.getCompletionDate() == null) {
	                existingMilestone.setCompletionDate(new Date()); // Set to current date only if not already set
	            }

	            // Save the updated milestone back to the database
	            taskMilestonesRepository.save(existingMilestone);
	            
	            // After saving the milestone, update the task status and check completion
	            updateTaskStatusAndCompletion(existingMilestone.getStudentTaskId());

	            // Update the user's badge based on completed milestones
	            updateUserBadge(existingMilestone.getStudentId()); // Ensure you have a way to get the userId from the milestone

	            return existingMilestone;
	        } else {
	            // Milestone not found
	            throw new RuntimeException("Milestone not found with id: " + milestoneId);
	        }
	    }

	    private void updateTaskStatusAndCompletion(String studentTaskId) {
	        List<TasksMilestones> milestones = taskMilestonesRepository.findByStudentTaskId(studentTaskId);
	        Optional<StudentTasks> taskOpt = studentTasksRepository.findById(studentTaskId);

	        if (taskOpt.isPresent()) {
	            StudentTasks task = taskOpt.get();

	            // Calculate completed milestones
	            long completedCount = milestones.stream().filter(TasksMilestones::getIsComplete).count();
	            
	            // Update the number of completed milestones
	            task.setMilestonesCompleted((int) completedCount);

	            // Update task status
	            if (completedCount == milestones.size()) {
	                task.setStatus("Completed");
	                task.setCompletionDate(new Date()); // Set completion to current date
	            } else if (completedCount > 0) {
	                task.setStatus("In Progress");
	            }

	            // Save the updated task
	            studentTasksRepository.save(task);
	            double completionPercentage = 100.0 * completedCount / milestones.size();
	            if (completionPercentage >= 80) {
	                notifyFriends(task);
	            }
	        } else {
	            throw new RuntimeException("Task not found with id: " + studentTaskId);
	        }
	    }
	    private void notifyFriends(StudentTasks task) {
	        Optional<User> userOpt = userRepository.findById(task.getStudentId()); // Assume userRepository is injected and fetches user details

	        if (userOpt.isPresent()) {
	            User user = userOpt.get();
	            List<StudentFriends> friendsList = studentFriendsRepository.findByStudentId(task.getStudentId());
	            
	            if (!friendsList.isEmpty()) {
	                String message = String.format("%s %s has completed 80%% of '%s' in course %s. Badge: %s, Rank: %s",
	                    user.getFirstName(), user.getLastName(), task.getTitle(), task.getCourseName(),
	                    user.getBadge(), user.getRank());  // Include badge and rank in the message

	                for (StudentFriends friends : friendsList) {
	                    for (Friend friend : friends.getFriends()) {
	                        emailService.sendSimpleMessage(friend.getEmail(), "Task Completion Update", message);
	                    }
	                }
	            }
	        } else {
	            // Handle the case where the user might not be found
	            throw new RuntimeException("User not found with id: " + task.getStudentId());
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
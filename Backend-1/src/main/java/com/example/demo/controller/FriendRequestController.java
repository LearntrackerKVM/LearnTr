package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.FriendRequest;
import com.example.demo.data.FriendRequestDTO;
import com.example.demo.service.FriendRequestService;


@RestController
@RequestMapping("/api/friendRequest")
@CrossOrigin("*")
public class FriendRequestController {

	@Autowired
	private FriendRequestService friendRequestService;
	
	
	@GetMapping("/{userId}/friendRequests")
	public ResponseEntity<List<FriendRequestDTO>> getFriendRequests(@PathVariable String userId) {
	    List<FriendRequestDTO> friendRequests = friendRequestService.getFriendRequestsForUser(userId);
	    return ResponseEntity.ok(friendRequests);
	}

	   @PostMapping("/send")
	    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest friendRequestDto) {
	        try {
	            friendRequestService.sendFriendRequest(friendRequestDto);
	            return ResponseEntity.ok().body("Friend request sent successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.badRequest().body("Error sending friend request: " + e.getMessage());
	        }
	    }

	   @PostMapping("/{friendRequestId}/accept")
	    public ResponseEntity<?> acceptFriendRequest(@PathVariable String friendRequestId) {
	        friendRequestService.acceptFriendRequest(friendRequestId);
	        return ResponseEntity.ok().body("Friend request accepted");
	    }
	   @PostMapping("/{friendRequestId}/reject")
	    public ResponseEntity<?> rejectFriendRequest(@PathVariable String friendRequestId) {
	        friendRequestService.rejectFriendRequest(friendRequestId);
	        return ResponseEntity.ok().body("Friend request rejected");
	    }
}

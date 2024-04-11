package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.data.FriendRequest;
import com.example.demo.data.FriendRequestDTO;
import com.example.demo.data.StudentFriends;
import com.example.demo.data.User;
import com.example.demo.data.Friend;
import com.example.demo.repository.FriendRequestRepository;
import com.example.demo.repository.StudentFriendsRepository;
import com.example.demo.repository.UserRepository;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentFriendsRepository studentFriendsRepository;

    public void sendFriendRequest(FriendRequest friendRequestDto) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderId(friendRequestDto.getSenderId());
        friendRequest.setRecipientId(friendRequestDto.getRecipientId());
        friendRequest.setStatus("pending");
        friendRequest.setCreatedAt(LocalDateTime.now());
        friendRequestRepository.save(friendRequest);
        // Fetch sender's user details
        User sender = userRepository.findById(friendRequestDto.getSenderId()).orElseThrow(() -> new RuntimeException("Sender not found"));

        // Construct the email body
        String emailBody = String.format("%s %s sent you a friend request.", sender.getFirstName(), sender.getLastName());

        // Assuming you have a method to get recipient email by recipientId
        User recipient = userRepository.findById(friendRequestDto.getRecipientId()).orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Send email
        emailService.sendSimpleMessage(recipient.getEmail(), "Friend Request", emailBody);
    }

    public List<FriendRequestDTO> getFriendRequestsForUser(String userId) {
        List<FriendRequest> requests = friendRequestRepository.findByRecipientIdAndStatus(userId, "pending");
        List<FriendRequestDTO> enrichedRequests = new ArrayList<>();

        for (FriendRequest request : requests) {
            User sender = userRepository.findById(request.getSenderId()).orElseThrow(() -> new RuntimeException("User not found"));
            FriendRequestDTO dto = new FriendRequestDTO(request, sender.getFirstName(), sender.getLastName(), sender.getEmail());
            enrichedRequests.add(dto);
        }

        return enrichedRequests;
    }
    

    public void acceptFriendRequest(String friendRequestId) {
        // Find the friend request by its ID
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        // Update the status of the friend request to "accepted"
        friendRequest.setStatus("accepted");
        friendRequestRepository.save(friendRequest);

        // Retrieve the sender and recipient details
        User sender = userRepository.findById(friendRequest.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User recipient = userRepository.findById(friendRequest.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Add each other as friends
        addFriend(sender, recipient);
        addFriend(recipient, sender);
    }
    private void addFriend(User user, User friend) {
        // Attempt to retrieve the StudentFriends document for the user, or create a new one if it doesn't exist
        StudentFriends studentFriends = studentFriendsRepository.findFirstByStudentId(user.getId())
                .orElse(new StudentFriends(user.getId(), user.getFirstName() + " " + user.getLastName(), new ArrayList<>()));

        // Create a new Friend instance for the friend to be added
        Friend newFriend = new Friend(friend.getId(), friend.getFirstName(), friend.getLastName(), friend.getEmail());

        // Add the new friend to the student's list of friends
        studentFriends.getFriends().add(newFriend);

        // Save the updated StudentFriends document back to the repository
        studentFriendsRepository.save(studentFriends);
    }

    public void rejectFriendRequest(String friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        friendRequest.setStatus("rejected");
        friendRequestRepository.save(friendRequest);
    }

}

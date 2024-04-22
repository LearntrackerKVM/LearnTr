package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.PasswordResetToken;
import com.example.demo.data.User;
import com.example.demo.data.UserResponse;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.*;


@Service
public class UserService {

	private final UserRepository userRepository;
	private final EmailService emailService;
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
	
	 
	    public UserService(UserRepository userRepository, EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository) {
	        this.userRepository = userRepository;
	        this.emailService = emailService;
	        this.passwordResetTokenRepository = passwordResetTokenRepository;
	    }
	    public UserResponse createUser(User user) {
	        // Check if a user with the same email exists
	        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
	        if (existingUserByEmail.isPresent()) {
	            return new UserResponse(false, "A user with the given email already exists.", null);
	        }

	        Optional<User> existingUserByUserName = userRepository.findByUserName(user.getUserName());
	        if (existingUserByUserName.isPresent()) {
	            return new UserResponse(false, "A user with the given username already exists.", null);
	        }

	        user.setId(null);
	        User savedUser = userRepository.save(user);

	        // Send a welcome email
	        emailService.sendSimpleMessage(
	                savedUser.getEmail(),
	                "Welcome to Learn Tracker",
	                "Dear " + savedUser.getUserName() + ",\n\nWelcome! We're glad you're here.");

	        return new UserResponse(true, "User created successfully.", savedUser);
	    }

	 
	    

	    public Iterable<User> getAllUsers() {
	        return userRepository.findAll();
	    }

	    public Optional<User> getUserById(String id) {
	        return userRepository.findById(id);
	    }

	    public User updateUser(String id, User user) {
	        user.setId(id); // Ensure the user's ID is set for the update operation
	        return userRepository.save(user);
	    }

	    public Optional<User> getUserByEmail(String email) {
	        return userRepository.findByEmail(email);
	    }
	    public List<User> getUsersByRole(String role) {
	        return userRepository.findByRole(role);
	    }

	     public void createPasswordResetTokenForUser(final String userEmail, final String token) {
	    	// Assuming this returns a String
	    	 User user = userRepository.findByEmail(userEmail)
	    	     .orElseThrow(() -> new UserNotFoundException("User not found  " + userEmail));

	         PasswordResetToken myToken = new PasswordResetToken();
	         myToken.setUserId(user.getId());
	         myToken.setToken(token);
	         myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours

	         passwordResetTokenRepository.save(myToken);

	         // Send email
	         emailService.sendSimpleMessage(
	             userEmail,
	             "Reset Password",
	             "To reset your password, click the link below:\n" + "http://localhost:4200/password-reset?token=" + token
	         );
	     }

	     public void resetPassword(final String token, final String newPassword) {
	         PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
	             .orElseThrow(() -> new IllegalArgumentException("Invalid password reset token"));

	         // Verify if token is expired
	         if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
	             throw new IllegalArgumentException("Token expired");
	         }

	         User user = userRepository.findById(resetToken.getUserId())
	                                   .orElseThrow(() -> new UserNotFoundException("User not found"));

	         user.setPassword(newPassword); // Make sure to encrypt this password
	         userRepository.save(user);
	         
	         // Optionally, invalidate the token after use
	     }
	     
	     public String saveProfilePicture(String userId, MultipartFile file) throws IOException {
	    	    Optional<User> userOptional = userRepository.findById(userId);
	    	    if (!userOptional.isPresent()) {
	    	        throw new RuntimeException("User not found");
	    	    }
	    	    User user = userOptional.get();

	    	    byte[] bytes = file.getBytes(); // Directly use byte array
	    	    user.setProfilePicture(bytes); // No conversion needed
	    	    userRepository.save(user);

	    	    return "/api/profilePicture/" + userId; // Assuming this endpoint serves the image
	    	}
	     public byte[] getProfilePicture(String userId) {
	    	    Optional<User> userOptional = userRepository.findById(userId);
	    	    if (!userOptional.isPresent()) {
	    	        throw new RuntimeException("User not found");
	    	    }
	    	    User user = userOptional.get();
	    	    byte[] imageBytes = user.getProfilePicture();
	    	    if (imageBytes == null || imageBytes.length == 0) {
	    	        throw new RuntimeException("Profile picture not set");
	    	    }
	    	    return imageBytes; // Directly return the byte array
	    	}


private String generateProfilePictureUrl(User user) {
    return "http://localhost:8080/users/" + user.getId() + "/profile-picture";
}


public void updateAllUserRanks() {
    // Step 1: Retrieve users ordered by milestone counts in descending order
    Query query = new Query().with(Sort.by(Sort.Direction.DESC, "milestonesCompleted"));
    List<User> users = mongoTemplate.find(query, User.class);

    // Step 2: Update each user's rank based on their position in the list
    for (int i = 0; i < users.size(); i++) {
        User user = users.get(i);
        Update update = new Update().set("rank", i + 1); // Rank is position in list + 1
        Query userQuery = Query.query(Criteria.where("id").is(user.getId()));
        mongoTemplate.updateFirst(userQuery, update, User.class);
    }
}

public void updateBadgeAndMilestoneCount(String userId, String badge, int milestoneCount) {
    Query query = new Query(Criteria.where("id").is(userId));
    Update update = new Update().set("badge", badge).set("milestonesCompleted", milestoneCount);
    mongoTemplate.findAndModify(query, update, User.class);

    // After updating, recalculate ranks for all users
    updateAllUserRanks();
}


	 }


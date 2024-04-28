package com.example.demo.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.User;
import com.example.demo.data.UserResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;
import org.springframework.http.MediaType;


@Controller
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

	private final UserService userService;
	
	@Autowired
	private EmailService emailService;

    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String emailToLower = user.getEmail().toLowerCase();
        Optional<User> foundUserOptional = userService.getUserByEmail(emailToLower);

        if (foundUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        User foundUser = foundUserOptional.get();

        if (!foundUser.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        } else {

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", foundUser);
            return ResponseEntity.ok(response);
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        UserResponse userResponse = userService.createUser(user);
        
        if (userResponse.isSuccess()) {
            // If user creation is successful, return the user data with CREATED status
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } else {
            // If there's an issue creating the user (e.g., username or email already exists),
            // return the error message with BAD_REQUEST or CONFLICT status
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(userResponse.getMessage());
        }
    }
    
    @GetMapping("/allstudents")
    public ResponseEntity<List<User>> getAllStudents() {
    	String role = "Student";
        List<User> students = userService.getUsersByRole(role);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
    

    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }
    @GetMapping("/byEmail")
    public ResponseEntity<Optional<User>> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<?> uploadProfilePicture(@RequestParam("userId") String userId, @RequestParam("file") MultipartFile file) {
        try {
            String profilePictureUrl = userService.saveProfilePicture(userId, file);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Profile picture updated successfully.");
            response.put("profilePictureUrl", profilePictureUrl); // Include the URL in the response
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update profile picture.");
        }
    }

    @GetMapping("/profilePicture/{userId}")
    public ResponseEntity<?> getProfilePicture(@PathVariable String userId) {
        try {
            byte[] imageBytes = userService.getProfilePicture(userId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Or MediaType.IMAGE_PNG
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
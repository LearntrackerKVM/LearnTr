package com.example.demo.controller;
import com.example.demo.data.PasswordResetToken;
import com.example.demo.data.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/users")
@CrossOrigin("*")
public class PasswordResetController {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    // Define a simple DTO for request body
    public static class PasswordResetRequest {
        private String email;

        // Getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
	
    public static class PasswordResetDTO {
        private String token;
        private String newPassword;

        // Getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    @PostMapping("/passwordresetrequest")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("No user associated with this email address.");
        }

        User user = userOptional.get();
        String resetToken = generateToken();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(resetToken);
        passwordResetToken.setUserId(user.getId()); // Assume User has an getId() method
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Token expires in 24 hours

        passwordResetTokenRepository.save(passwordResetToken); // Assume you have a repository for these

        String resetLink = "http://localhost:4200/passwordreset?token=" + resetToken;
        String emailBody = "Please click on the following link to reset your password: " + resetLink;

        emailService.sendSimpleMessage(user.getEmail(), "Password Reset Request", emailBody);

        return ResponseEntity.ok().body("Password reset link has been sent to your email.");
    }


	    public static String generateToken() {
	        return UUID.randomUUID().toString();
	    }
	    
	    @PostMapping("/reset-password")
	    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
	        userService.resetPassword(passwordResetDTO.getToken(), passwordResetDTO.getNewPassword());
	        return ResponseEntity.ok("Password has been reset.");
	    }

}

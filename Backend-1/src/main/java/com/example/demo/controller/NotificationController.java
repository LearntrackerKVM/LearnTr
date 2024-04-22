package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.data.Notification;

@Controller
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate template;
    // Initialize Notifications
    private Notification notifications = new Notification(0);
    
    
    
    @GetMapping("/notify")
    public String getNotification() {
        // Increment Notification by one
        notifications.increment();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", notifications);
        return "Notifications successfully sent to Angular !";
    }
}


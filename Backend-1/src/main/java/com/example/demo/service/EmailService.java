package com.example.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("your-email@example.com");
            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = "<html><body><img src='cid:logo' alt='Brand Logo' width='40' height='30'><p>" + text + "</p></body></html>";
            helper.setText(htmlContent, true); // Set to true to send HTML

            ClassPathResource logo = new ClassPathResource("static/brand.png");
            helper.addInline("logo", logo); 

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

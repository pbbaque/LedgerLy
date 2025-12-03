package com.backend.api.invoice_manager.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    
    @Override
    public void sendUserCredentials(String to, String name, String tempPassword, String companyName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to YourApp – Access Details");
        message.setText(
            "Hi " + name + ",\n\n" +
            "Your company \"" + companyName + "\" has been successfully registered in our system.\n\n" +
            "Here are your login credentials:\n" +
            "- Email: " + to + "\n" +
            "- Temporary password: " + tempPassword + "\n\n" +
            "Access the platform at: https://yourapp.com/login\n\n" +
            "Please change your password after logging in.\n\n" +
            "Thank you,\nYourApp Team"
        );
        javaMailSender.send(message);
    }

     @Override
     public void sendEmail(String to, String subject, String body, String companyName) {
         SimpleMailMessage message = new SimpleMailMessage();
         message.setTo(to);
         message.setSubject(subject);
         message.setText(
             "Hi,\n\n" +
             body + "\n\n" +
             "Thank you,\n" + companyName + " Team"
         );
         javaMailSender.send(message);
     }

    @Override
    public void sendPasswordResetLink(String to, String tempPassword, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText(
            "Hi " + username + ",\n\n" +
            "We received a request to reset your password. Here is your temporary password:\n" +
            "- Temporary password: " + tempPassword + "\n\n" +
            "Please log in and change your password as soon as possible.\n\n" +
            "Thank you,\nYourApp Team"
        );
        javaMailSender.send(message);
    }
}

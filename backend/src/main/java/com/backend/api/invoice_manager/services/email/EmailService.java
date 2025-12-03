package com.backend.api.invoice_manager.services.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body, String companyName);

    void sendUserCredentials(String to, String name, String tempPassword, String companyName);

    void sendPasswordResetLink(String to, String tempPassword, String username);

}

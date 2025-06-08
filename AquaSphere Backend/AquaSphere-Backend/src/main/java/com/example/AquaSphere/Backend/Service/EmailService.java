package com.example.AquaSphere.Backend.Service;

public interface EmailService {
    void sendOTPEmail(String to, String otp);
    void sendPasswordResetEmail(String to, String resetLink);

    void sendEmail(String email, String subject, String body);
}
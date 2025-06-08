package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOTPEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("AquaSphere - Your Verification Code");

            String htmlMsg = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                    + "<h1 style='color: #0a0a5a; text-align: center;'>AquaSphere</h1>"
                    + "<div style='background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin-top: 20px;'>"
                    + "<h2 style='color: #0a0a5a; margin-bottom: 20px;'>Verification Code</h2>"
                    + "<p>Your verification code is:</p>"
                    + "<h1 style='text-align: center; font-size: 32px; letter-spacing: 5px; background-color: #e9ecef; padding: 10px; border-radius: 5px;'>"
                    + otp + "</h1>"
                    + "<p style='margin-top: 20px;'>This code will expire in 10 minutes.</p>"
                    + "</div>"
                    + "<p style='text-align: center; margin-top: 20px; color: #6c757d; font-size: 12px;'>"
                    + "If you did not request this code, please ignore this email.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("AquaSphere - Password Reset Request");

            String htmlMsg = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>"
                    + "<h1 style='color: #0a0a5a; text-align: center;'>AquaSphere</h1>"
                    + "<div style='background-color: #f8f9fa; padding: 20px; border-radius: 5px; margin-top: 20px;'>"
                    + "<h2 style='color: #0a0a5a; margin-bottom: 20px;'>Password Reset</h2>"
                    + "<p>You have requested to reset your password. Click the button below to create a new password:</p>"
                    + "<div style='text-align: center; margin: 30px 0;'>"
                    + "<a href='" + resetLink + "' style='background-color: #0a0a5a; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>"
                    + "Reset Password</a>"
                    + "</div>"
                    + "<p>This link will expire in 30 minutes.</p>"
                    + "</div>"
                    + "<p style='text-align: center; margin-top: 20px; color: #6c757d; font-size: 12px;'>"
                    + "If you didn't request a password reset, you can safely ignore this email.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
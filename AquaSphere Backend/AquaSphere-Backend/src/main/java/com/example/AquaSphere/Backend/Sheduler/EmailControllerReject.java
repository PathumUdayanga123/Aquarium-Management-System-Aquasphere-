package com.example.AquaSphere.Backend.Sheduler;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Repository.ServiceReminderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class EmailControllerReject {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ServiceReminderRepository serviceReminderRepository;

    @PostMapping("/reminder/send-email-reject/{service_id}")
    public String sendEmail(@PathVariable("service_id") int service_id) {
        try {
            Optional<ServiceReminderEntity> reminderOptional = serviceReminderRepository.findById(service_id);

            if (reminderOptional.isPresent()) {
                ServiceReminderEntity serviceReminderEntity = reminderOptional.get();

                String userEmail = serviceReminderEntity.getApplicant_email(); // Corrected method

                if (userEmail == null || userEmail.isEmpty()) {
                    return "Email address not found for service_id: " + service_id;
                }

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("bandarapathum123@gmail.com");
                message.setTo(userEmail);
                message.setSubject("Email From Wasantha Aquarium");

                LocalDateTime now = LocalDateTime.now();
                String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                message.setText("Hello, \n\nService Request Rejection from Wasantha Aquarium \n\nDear "
                        + serviceReminderEntity.getApplicant_name() + " \n\nThank you for your service request to Wasantha Aquarium. Unfortunately, we are unable to accept your request at this time, as we already have another booking for the same period.\n\nWe kindly recommend that you check our available service dates and feel free to book again for a more suitable time.\n\nFor any further inquiries or assistance, please do not hesitate to contact us at 0772242286.\n\nThank you for understanding, and we look forward to serving you in the future. \n\nBest regards,\nThe Wasantha Aquarium Team "
                        + serviceReminderEntity.getService_time()
                        + ".\n\nSent on: " + formattedDateTime);

                // Send the email
                mailSender.send(message);

                // Update status
                serviceReminderEntity.setService_status("SENT");
                serviceReminderRepository.save(serviceReminderEntity);

                return "Email sent successfully to: " + userEmail;
            } else {
                return "No user found with service_id: " + service_id;
            }
        } catch (Exception e) {
            return "Error while sending email: " + e.getMessage();
        }
    }
}

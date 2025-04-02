package com.example.AquaSphere.Backend.Sheduler;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Repository.ServiceReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/reminder")
public class EmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ServiceReminderRepository serviceReminderRepository;

    @PostMapping("/send-email/{service_id}")
    public String sendEmail(@PathVariable("service_id") int service_id) {
        try {
            Optional<ServiceReminderEntity> reminderOptional = serviceReminderRepository.findById(service_id);

            if (reminderOptional.isPresent()) {
                ServiceReminderEntity serviceReminderEntity = reminderOptional.get();
                String userEmail = serviceReminderEntity.getApplicant_email();

                if (userEmail == null || userEmail.isEmpty()) {
                    return "Email address not found for service_id: " + service_id;
                }

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("your-email@example.com"); // Replace with your email
                message.setTo(userEmail);
                message.setSubject("Email From Wasantha Aquarium");

                LocalDateTime now = LocalDateTime.now();
                String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                message.setText("Hello,\n\nService Request Confirmation from Wasantha Aquarium\n\nDear "
                        + serviceReminderEntity.getApplicant_name() + ",\n\nWe are pleased to inform you that we have accepted your service request for "
                        + serviceReminderEntity.getService_type() + " services. We will contact you soon.\n\n"
                        + "Service Time: " + serviceReminderEntity.getService_time() + "\n"
                        + "Sent on: " + formattedDateTime + "\n\n"
                        + "Best regards,\nThe Wasantha Aquarium Team");

                mailSender.send(message);

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

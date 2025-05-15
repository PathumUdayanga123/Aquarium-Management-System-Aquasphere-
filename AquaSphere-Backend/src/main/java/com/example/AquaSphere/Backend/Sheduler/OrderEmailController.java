package com.example.AquaSphere.Backend.Sheduler;
import com.example.AquaSphere.Backend.Entity.OrderEntity;
import com.example.AquaSphere.Backend.Repository.OrderRepository;
import com.example.AquaSphere.Backend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/email")
public class OrderEmailController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    // === Send Order Receipt Email ===
    @PostMapping("/send-order-receipt/{orderId}")
    public String sendOrderReceiptEmail(@PathVariable("orderId") Long orderId) {
        try {
            Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isEmpty()) {
                return "No order found with orderId: " + orderId;
            }

            OrderEntity order = orderOptional.get();
            String userEmail = order.getUserEmail();

            if (userEmail == null || userEmail.trim().isEmpty()) {
                return "Email address not found for orderId: " + orderId;
            }

            // Format current date and time
            String formattedDateTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Build email message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@example.com"); // Replace with your sender email
            message.setTo(userEmail);
            message.setSubject("Your Order Receipt - Wasantha Aquarium");

            String emailBody = String.format(
                    "Hello %s,\n\nThank you for your order!\n\n" +
                            "Order Details:\n" +
                            "• Item: %s\n" +
                            // Removed Description line
                            "• Price: Rs. %d\n" +
                            "• Quantity: %d\n" +
                            "• Package Charge: Rs. %.2f\n" +
                            "• Delivery Fee: Rs. %.2f\n" +
                            "• Total: Rs. %.2f\n" +
                            "• Delivery Address: %s, %s\n\n" +
                            "We will notify you once your order is packaged.\n\n" +
                            "Sent on: %s\n\n" +
                            "Best regards,\nWasantha Aquarium Team",
                    order.getUserName(),
                    order.getItemname(),
                    order.getItemprice(),
                    order.getQuantity(), // Changed from getStockcount to getQuantity
                    order.getPackagecharge(),
                    order.getDeliveryfee(),
                    order.getTotalPrice(),
                    order.getUserAddress(),
                    order.getDistrict(),
                    formattedDateTime
            );

            message.setText(emailBody);
            mailSender.send(message);

            return "Order receipt email sent successfully to: " + userEmail;

        } catch (Exception e) {
            return "Error while sending order receipt email: " + e.getMessage();
        }
    }

    // === Send Packaging Complete Email ===
    @PostMapping("/send-packaging-email/{orderId}")
    public String sendPackagingEmail(@PathVariable("orderId") Long orderId) {
        try {
            Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);

            if (orderOptional.isEmpty()) {
                return "No order found with orderId: " + orderId;
            }

            OrderEntity order = orderOptional.get();
            String userEmail = order.getUserEmail();

            if (userEmail == null || userEmail.trim().isEmpty()) {
                return "Email address not found for orderId: " + orderId;
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@example.com"); // Replace with your sender email
            message.setTo(userEmail);
            message.setSubject("Packaging Complete - Wasantha Aquarium");

            String emailBody = String.format(
                    "Hello %s,\n\nGood news! Your order for \"%s\" has been packaged and is ready for delivery.\n\n" +
                            "We'll get it to you shortly.\n\n" +
                            "Thank you for choosing Wasantha Aquarium!\n\nBest regards,\nWasantha Aquarium Team",
                    order.getUserName(),
                    order.getItemname()
            );

            message.setText(emailBody);
            mailSender.send(message);

            return "Packaging email sent successfully to: " + userEmail;

        } catch (Exception e) {
            return "Error while sending packaging email: " + e.getMessage();
        }
    }
}
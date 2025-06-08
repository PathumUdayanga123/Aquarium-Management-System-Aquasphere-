package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.DTO.OrderDetailsDTO;
import com.example.AquaSphere.Backend.Entity.OrderDetails;
import com.example.AquaSphere.Backend.Repository.OrderDetailsRepository;
import com.example.AquaSphere.Backend.Service.EmailService;
import com.example.AquaSphere.Backend.Service.OrderDetailsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final EmailService emailService;

    @Autowired
    public OrderDetailsServiceImpl(OrderDetailsRepository orderDetailsRepository, EmailService emailService) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public OrderDetails createOrder(OrderDetailsDTO orderDetailsDTO) {
        try {
            // Create and save order
            OrderDetails orderDetails = new OrderDetails();
            BeanUtils.copyProperties(orderDetailsDTO, orderDetails);
            OrderDetails savedOrder = orderDetailsRepository.save(orderDetails);

            // Send email confirmation
            sendOrderConfirmationEmail(savedOrder);

            return savedOrder;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    @Override
    public List<OrderDetails> getOrdersByUserId(Long userId) {
        return orderDetailsRepository.findByUserId(userId);
    }

    @Override
    public List<OrderDetails> getAllOrders() {
        return orderDetailsRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        if (!orderDetailsRepository.existsById(orderId)) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }

        // Get order info for email notification
        OrderDetails order = orderDetailsRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Delete the order
        orderDetailsRepository.deleteById(orderId);

        // Send cancellation email
        sendOrderCancellationEmail(order);
    }

    private void sendOrderConfirmationEmail(OrderDetails order) {
        try {
            String subject = "AquaSphere - Order Confirmation #" + order.getOrderid();
            String body = "Dear " + order.getName() + ",\n\n" +
                    "Thank you for your order! Your order has been received and is being processed.\n\n" +
                    "Order Details:\n" +
                    "Order ID: " + order.getOrderid() + "\n" +
                    "Item: " + order.getItemName() + "\n" +
                    "Quantity: " + order.getQuantity() + "\n" +
                    "Price: Rs." + order.getItemPrice() + "\n" +
                    "Total: Rs." + order.getTotalPrice() + "\n\n" +
                    "Shipping Address:\n" +
                    order.getAddress() + "\n" +
                    order.getDistrict() + " " + order.getPostalCode() + "\n\n" +
                    "We will notify you when your order ships.\n\n" +
                    "Thank you for shopping with AquaSphere!\n" +
                    "AquaSphere Team";

            emailService.sendEmail(order.getEmail(), subject, body);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent order creation failure
            System.err.println("Failed to send order confirmation email: " + e.getMessage());
        }
    }

    private void sendOrderCancellationEmail(OrderDetails order) {
        try {
            String subject = "AquaSphere - Order Cancellation #" + order.getOrderid();
            String body = "Dear " + order.getName() + ",\n\n" +
                    "Your order has been cancelled successfully.\n\n" +
                    "Cancelled Order Details:\n" +
                    "Order ID: " + order.getOrderid() + "\n" +
                    "Item: " + order.getItemName() + "\n" +
                    "Quantity: " + order.getQuantity() + "\n" +
                    "Total: Rs." + order.getTotalPrice() + "\n\n" +
                    "If you did not request this cancellation, please contact our customer support immediately.\n\n" +
                    "Thank you for your interest in AquaSphere!\n" +
                    "AquaSphere Team";

            emailService.sendEmail(order.getEmail(), subject, body);
        } catch (Exception e) {
            // Log the error but don't throw it
            System.err.println("Failed to send order cancellation email: " + e.getMessage());
        }
    }
}
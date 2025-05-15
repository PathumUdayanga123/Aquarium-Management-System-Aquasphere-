package com.example.AquaSphere.Backend.ServiceImplementation;

import com.example.AquaSphere.Backend.DTO.OrderItemDTO;
import com.example.AquaSphere.Backend.DTO.ProfitReportDTO;
import com.example.AquaSphere.Backend.Entity.OrderEntity;
import com.example.AquaSphere.Backend.Repository.OrderRepository;
import com.example.AquaSphere.Backend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public OrderEntity placeOrder(OrderItemDTO orderItemDTO) {
        OrderEntity order = new OrderEntity();
        order.setUserId(orderItemDTO.getUserId());
        order.setUserName(orderItemDTO.getUserName());
        order.setUserEmail(orderItemDTO.getUserEmail());
        order.setUserAddress(orderItemDTO.getUserAddress());
        order.setUserPhone(orderItemDTO.getUserPhone());
        order.setPostalcode(orderItemDTO.getPostalcode());

        order.setItemcode(orderItemDTO.getItemcode());
        order.setItemprice(orderItemDTO.getItemprice());
        order.setItemtype(orderItemDTO.getItemtype());
        order.setItemname(orderItemDTO.getItemname());
        order.setStockcount(orderItemDTO.getStockcount());

        order.setDistrict(orderItemDTO.getDistrict());
        order.setOrderstatus(orderItemDTO.getOrderstatus());
        order.setInitialCost(orderItemDTO.getInitialCost());

        order.setOrderDate(LocalDate.now());
        order.setOrderTime(LocalTime.now());

        // Set delivery fee
        if ("Colombo".equalsIgnoreCase(orderItemDTO.getDistrict())) {
            order.setDeliveryfee(500.00);
        } else {
            order.setDeliveryfee(2500.00);
        }

        // Set package charge only for fish items
        double packageCharge = 0.0; // Default for non-fish items
        if ("fish".equalsIgnoreCase(orderItemDTO.getItemtype())) {
            packageCharge = orderItemDTO.getPackagecharge() != null ?
                    orderItemDTO.getPackagecharge() : 1000.0;
        }
        order.setPackagecharge(packageCharge);

        // Calculate total price
        double total = (orderItemDTO.getItemprice() * orderItemDTO.getStockcount())
                + packageCharge + order.getDeliveryfee();
        order.setTotalPrice(total);

        OrderEntity savedOrder = orderRepository.save(order);

        sendReceiptEmail(savedOrder); // send email

        return savedOrder;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            LocalDateTime orderDateTime = LocalDateTime.of(order.getOrderDate(), order.getOrderTime());
            LocalDateTime now = LocalDateTime.now();

            Duration duration = Duration.between(orderDateTime, now);
            if (duration.toHours() <= 24) {
                orderRepository.deleteById(orderId);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderEntity> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public boolean sendPackagingEmail(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("your-email@example.com");
                message.setTo(order.getUserEmail());
                message.setSubject("Packaging Complete - Wasantha Aquarium");

                String text = "Hello " + order.getUserName() + ",\n\n"
                        + "Good news! Your order for \"" + order.getItemname() + "\" has been packaged and is ready for delivery.\n\n"
                        + "We'll get it to you shortly.\n\n"
                        + "Thank you for choosing Wasantha Aquarium!\n\n"
                        + "Best regards,\nThe Wasantha Aquarium Team";

                message.setText(text);
                mailSender.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void sendReceiptEmail(OrderEntity order) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("your-email@example.com");
            message.setTo(order.getUserEmail());
            message.setSubject("Your Order Receipt - Wasantha Aquarium");

            String text = "Hello " + order.getUserName() + ",\n\n"
                    + "Thank you for your order!\n\n"
                    + "Order Details:\n"
                    + "- Item: " + order.getItemname() + "\n"

                    + "- Price: Rs. " + order.getItemprice() + "\n"
                    + "- Quantity: " + order.getStockcount() + "\n"
                    + "- Package Charge: Rs. " + order.getPackagecharge() + "\n"
                    + "- Delivery Fee: Rs. " + order.getDeliveryfee() + "\n"
                    + "- Total: Rs. " + order.getTotalPrice() + "\n"
                    + "- Delivery Address: " + order.getUserAddress() + ", " + order.getDistrict() + "\n\n"
                    + "We will notify you once your order is packaged.\n\n"
                    + "Best regards,\nWasantha Aquarium Team";

            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProfitReportDTO> getDailyProfit(LocalDate date) {
        List<OrderEntity> orders = orderRepository.findByOrderDate(date);
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        double totalProfit = calculateTotalProfit(orders);

        return Collections.singletonList(new ProfitReportDTO(date, totalProfit));
    }

    @Override
    public List<ProfitReportDTO> getMonthlyProfit(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        List<OrderEntity> monthlyOrders = orderRepository.findByOrderDateBetween(startDate, endDate);

        // If no orders found for the month
        if (monthlyOrders.isEmpty()) {
            return Collections.emptyList();
        }

        // Group orders by date
        Map<LocalDate, List<OrderEntity>> ordersByDate = monthlyOrders.stream()
                .collect(Collectors.groupingBy(OrderEntity::getOrderDate));

        // Calculate profit for each day
        List<ProfitReportDTO> dailyProfits = new ArrayList<>();
        for (Map.Entry<LocalDate, List<OrderEntity>> entry : ordersByDate.entrySet()) {
            LocalDate orderDate = entry.getKey();
            List<OrderEntity> ordersList = entry.getValue();
            double profit = calculateTotalProfit(ordersList);
            dailyProfits.add(new ProfitReportDTO(orderDate, profit));
        }

        // Sort by date
        dailyProfits.sort(Comparator.comparing(ProfitReportDTO::getDate));

        return dailyProfits;
    }

    // Helper method to calculate profit for a list of orders
    private double calculateTotalProfit(List<OrderEntity> orders) {
        return orders.stream()
                .mapToDouble(order -> {
                    // Get total price (income)
                    double income = order.getTotalPrice() != null ? order.getTotalPrice() : 0.0;

                    // Get initial cost (expense)
                    double cost = 0.0;
                    if (order.getInitialCost() != null) {
                        cost = order.getInitialCost();
                    }

                    // Calculate profit
                    return income - cost;
                })
                .sum();
    }
    @Override
    public boolean updateOrderStatus(Long orderId, String status) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            order.setOrderstatus(status);
            orderRepository.save(order);
            return true;
        }
        return false;
    }
}
package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.ProfitReportDTO;
import com.example.AquaSphere.Backend.Entity.OrderEntity;
import com.example.AquaSphere.Backend.DTO.OrderItemDTO;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    OrderEntity placeOrder(OrderItemDTO orderItemDTO);

    boolean cancelOrder(Long orderId);

    List<OrderEntity> getAllOrders();

    List<OrderEntity> getOrdersByUserId(Long userId);

    boolean sendPackagingEmail(Long orderId);

    List<ProfitReportDTO> getDailyProfit(LocalDate date);

    List<ProfitReportDTO> getMonthlyProfit(int year, int month);

    boolean updateOrderStatus(Long orderId, String status);
}
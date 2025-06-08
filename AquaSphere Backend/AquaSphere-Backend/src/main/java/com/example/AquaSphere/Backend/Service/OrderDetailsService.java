package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.OrderDetailsDTO;
import com.example.AquaSphere.Backend.Entity.OrderDetails;

import java.util.List;

public interface OrderDetailsService {
    OrderDetails createOrder(OrderDetailsDTO orderDetailsDTO);
    List<OrderDetails> getOrdersByUserId(Long userId);
    List<OrderDetails> getAllOrders();
    void deleteOrder(Long orderId);
}
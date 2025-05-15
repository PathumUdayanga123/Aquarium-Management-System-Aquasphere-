package com.example.AquaSphere.Backend.Controller;
import com.example.AquaSphere.Backend.DTO.OrderItemDTO;
import com.example.AquaSphere.Backend.DTO.ProfitReportDTO;
import com.example.AquaSphere.Backend.Entity.OrderEntity;
import com.example.AquaSphere.Backend.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order_details")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderEntity> placeOrder(@RequestBody OrderItemDTO orderDTO) {
        OrderEntity savedOrder = orderService.placeOrder(orderDTO);
        return ResponseEntity.ok(savedOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        boolean isCancelled = orderService.cancelOrder(orderId);
        if (isCancelled) {
            return ResponseEntity.ok("Order cancelled successfully.");
        } else {
            return ResponseEntity.badRequest().body("Order cancellation period expired or Order ID not found.");
        }
    }

    // Admin view: get all orders
    @GetMapping("/all")
    public ResponseEntity<List<OrderEntity>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // User view: get orders by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderEntity>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();  // Returns 204 if no orders are found
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/profit/daily")
    public ResponseEntity<ProfitReportDTO> getDailyProfit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ProfitReportDTO> profitReport = orderService.getDailyProfit(date);
        if (profitReport.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(profitReport.get(0));  // Return the first item from list
    }

    @GetMapping("/profit/monthly")
    public ResponseEntity<List<ProfitReportDTO>> getMonthlyProfit(
            @RequestParam int year,
            @RequestParam int month) {
        List<ProfitReportDTO> monthlyProfit = orderService.getMonthlyProfit(year, month);
        if (monthlyProfit.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(monthlyProfit);
    }
    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> statusUpdate) {

        String newStatus = statusUpdate.get("status");
        boolean updated = orderService.updateOrderStatus(orderId, newStatus);

        if (updated) {
            return ResponseEntity.ok("Order status updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update order status");
        }
    }


}
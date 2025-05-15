package com.example.AquaSphere.Backend.DTO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private Long userId;
    private String userName;
    private String userEmail;
    private String userAddress;
    private String userPhone;
    private String postalcode; // Changed to String to handle letters too

    private String itemcode;
    private int itemprice;
    private String itemtype;
    private String itemname;
    // Removed itemdescription field
    private int quantity; // Changed from stockcount to quantity
    private int stockcount; // Added new stockcount field

    private Double totalPrice;
    private String district;
    private Double deliveryfee;
    private Double packagecharge;
    private LocalDate orderDate;
    private LocalTime orderTime;
    private String orderstatus;
    private int initialCost;
}
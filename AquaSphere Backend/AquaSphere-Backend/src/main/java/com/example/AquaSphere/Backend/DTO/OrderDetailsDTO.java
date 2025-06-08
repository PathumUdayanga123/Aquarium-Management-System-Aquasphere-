// OrderDetailsDTO.java
package com.example.AquaSphere.Backend.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderDetailsDTO {
    private Long userId;
    private String name;
    private String email;
    private String address;
    private String contactNo;
    private String postalCode;
    private String district;
    private String itemCode;
    private String itemType;
    private String itemName;
    private Double itemPrice;
    private String itemPhoto;
    private Integer quantity;
    private Double totalPrice;
    private String orderstatus;
    private LocalDateTime orderDate;
}
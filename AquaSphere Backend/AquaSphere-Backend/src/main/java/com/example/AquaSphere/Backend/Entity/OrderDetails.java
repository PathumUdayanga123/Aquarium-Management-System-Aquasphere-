package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderid;

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

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String itemPhoto;

    private Integer quantity;
    private Double totalPrice;
    private String orderstatus;
    private LocalDateTime orderDate = LocalDateTime.now();
}
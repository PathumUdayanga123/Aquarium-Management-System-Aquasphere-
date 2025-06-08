package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shopping_cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;
    private String itemName;
    private double itemPrice;
    private int quantity;
    private double totalAmount;

    @Lob
    @Column(name = "item_photo", columnDefinition = "LONGBLOB")
    private byte[] itemPhoto; // Store image as byte array (BLOB)

    public void calculateTotalAmount() {
        this.totalAmount = itemPrice * quantity;
    }
}

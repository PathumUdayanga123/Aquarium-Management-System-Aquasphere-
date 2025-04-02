package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "inventory_fish")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemType;

    private String itemName;
    private LocalDate itemAddDate;
    private String itemCode;
    private String itemDescription;
    private double itemPrice;
    private int stockCount;
    private double discount;

    @Lob
    @Column(name = "item_photo", columnDefinition = "LONGBLOB")
    private byte[] itemPhoto;

    private double totalAmount; // New field to store calculated total amount

    @Column(name = "stock_status")
    private String stockStatus; // Persist the stock status to the DB

    public void calculateTotalAmount() {
        this.totalAmount = itemPrice - (itemPrice * (discount / 100)); // Calculate and set the total amount
    }

    public boolean isSoldOut() {
        return stockCount == 0;
    }

    // Method to update stock status based on stock count
    public void updateStockStatus() {
        if (this.stockCount == 0) {
            this.stockStatus = "Out of Stock";
        } else {
            this.stockStatus = "In Stock";
        }
    }
}

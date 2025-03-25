package com.example.AquaSphere.Backend.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InventoryDto {
    private String itemType;
    private String itemName;
    private LocalDate itemAddDate;
    private String itemCode;
    private String itemDescription;
    private double itemPrice;
    private int stockCount;
    private double discount;
    private byte[] itemPhoto;
}

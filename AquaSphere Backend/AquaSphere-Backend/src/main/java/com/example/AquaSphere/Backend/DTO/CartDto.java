package com.example.AquaSphere.Backend.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CartDto {
    private String itemCode;
    private String itemName;
    private double itemPrice;
    private int quantity;
    private MultipartFile itemPhoto; // Handle image as MultipartFile
}

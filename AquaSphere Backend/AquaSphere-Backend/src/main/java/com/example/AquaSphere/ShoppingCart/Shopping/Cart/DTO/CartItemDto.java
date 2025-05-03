package com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;

    public <R> CartItemDto(Long id, R getitemName, String s, String s1, Integer quantity, Integer getitemPrice, int i) {
    }

    public Long getId() {
        return id;
    }
    public Long getProductId() {
        return productId;
    }
    public String getProductName() {

        return productName;
    }
    public String getProductImageUrl(){
        return productImageUrl;
    }
    public Double getUnitPrice(){
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }


    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CartItemDto(Long id, Long productId, String productName,
                       String imageUrl, Integer quantity,
                       Double unitPrice, double subtotal){
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = imageUrl;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;


    }
}

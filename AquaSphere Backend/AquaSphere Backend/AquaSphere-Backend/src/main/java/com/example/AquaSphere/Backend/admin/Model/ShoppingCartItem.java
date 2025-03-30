package com.example.AquaSphere.Backend.admin.Model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shopping_cart_items")
public class ShoppingCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "adminId", nullable = false)
    private Long adminId;

    public ShoppingCartItem(){}

    public ShoppingCartItem(Long productId, int quantity, BigDecimal price,Long adminId){
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.adminId = adminId;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getProductId(){
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getAdminId(){
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}

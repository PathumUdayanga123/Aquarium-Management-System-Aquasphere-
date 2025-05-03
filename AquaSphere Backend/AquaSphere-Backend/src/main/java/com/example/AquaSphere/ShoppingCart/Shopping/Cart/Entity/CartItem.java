package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_id",nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String itemType;
    private String itemName;
    private LocalDateTime itemAddDate;

    private String itemCode;
    private String itemDescription;
    private Double itemPrice;
    private Double discount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Product getProduct() {
        return product;
    }

    public String getItemName() {
        return itemType;
    }

    public LocalDateTime getitemAddDate(){
        return itemAddDate;
    }
    public Integer getQuantity(){
        return quantity;
    }
    public String getItemCode(){

        return itemCode;
    }
    public String getItemDescription(){
        return itemDescription;
    }
    public Double getItemPrice() {
        return itemPrice;
    }
    public Double getDiscount(){
        return discount;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUserId(Long userId){
        this.userId=userId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setitemType(String itemType) {
        this.itemType = itemType;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setItemAddDate(LocalDateTime itemAddDate) {
        this.itemAddDate = itemAddDate;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    public void setItemPrice(Double itemPrice) {
        this.itemPrice = itemPrice;
    }
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setItemName() {
    }

    public void setItemAddDate() {
    }

    public void setQuantity() {
    }

    public void setItemCode() {
    }

    public void setItemDescription() {
    }

    public void setItemPrice() {
    }

    public void setDiscount() {
    }

    public <R> R getitemName() {
        return null;
    }

    public Integer getitemPrice() {
        return null;
    }

    public Object getitemType() {
        return null;
    }

    public void setitemType() {
    }
}


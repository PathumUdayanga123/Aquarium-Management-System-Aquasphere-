package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderid;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name = "USER_ADDRESS")
    private String userAddress;

    @Column(name = "USER_PHONE")
    private String userPhone;

    @Column(name = "POSTAL_CODE", length = 10)
    private String postalcode;

    @Column(name = "ITEM_CODE")
    private String itemcode;

    @Column(name = "ITEM_PRICE")
    private Integer itemprice;  // Changed from int to Integer

    @Column(name = "ITEM_TYPE")
    private String itemtype;

    @Column(name = "ITEM_NAME")
    private String itemname;

    // Removed itemdescription field

    @Column(name = "QUANTITY")
    private Integer quantity;  // Changed from STOCK_COUNT to QUANTITY

    @Column(name = "STOCK_COUNT")
    private Integer stockcount;  // Added new STOCK_COUNT attribute

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "DELIVERY_FEE")
    private Double deliveryfee;

    @Column(name = "PACKAGE_CHARGE")
    private Double packagecharge;

    @Column(name = "ORDER_DATE")
    private LocalDate orderDate;

    @Column(name = "ORDER_TIME")
    private LocalTime orderTime;

    @Column(name = "ORDER_STATUS")
    private String orderstatus;

    @Column(name = "INITIAL_COST")
    private Integer initialCost;  // Changed from int to Integer

    public Integer getItemCost() {
        return initialCost;
    }

    public void setItemCost(Integer itemCost) {
        this.initialCost = itemCost;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }
}

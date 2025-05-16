package com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDto {
    private Long productId;
    private Integer quantity;

    public Long getProductId(){

        return productId;
    }
    public Integer getQuantity(){

        return quantity;
    }
}


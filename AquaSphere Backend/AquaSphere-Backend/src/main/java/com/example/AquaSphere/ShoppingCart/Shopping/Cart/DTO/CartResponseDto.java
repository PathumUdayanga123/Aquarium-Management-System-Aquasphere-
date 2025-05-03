package com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {
    private List<CartItemDto> cartItems;
    private Double total;
    private Integer itemCount;

    public CartResponseDto(List<CartItemDto> cartItems, double total,int itemCount){
        this.cartItems = cartItems;
        this.total = total;
        this.itemCount = itemCount;

    }
}

package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Service;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.AddToCartDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.CartResponseDto;

public interface CartService {
    CartResponseDto getCart(Long userId);
    CartResponseDto addToCart(Long userId, AddToCartDto addToCartDto);
    CartResponseDto updateCartItem(Long userId, Long itemId, Integer quantity);
    CartResponseDto removeFromCart(Long userId, Long itemId);
    void clearCart(Long userId);
}

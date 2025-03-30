package com.example.AquaSphere.Backend.User.Service;

import com.example.AquaSphere.Backend.User.DTO.ShoppingCartDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShoppingCartService {
    @Transactional
    ShoppingCartDTO addToCart();

    ShoppingCartDTO addToCart(ShoppingCartDTO cartItem);

    void removeFromCart(Long userId, Long productId);

    List<ShoppingCartDTO> getCartItems(Long userId);

    void updateCartItemQuantity(Long userId, Long productId, int quantity);

    void clearCart(Long userId);

}
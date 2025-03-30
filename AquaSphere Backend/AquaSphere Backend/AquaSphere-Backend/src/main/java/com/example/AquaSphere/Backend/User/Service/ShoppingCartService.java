package com.example.AquaSphere.Backend.User.Service;

import com.example.AquaSphere.Backend.User.DTO.ShoppingCartDTO;
import com.example.AquaSphere.Backend.admin.Model.ShoppingCartItem;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingCartService {
    @Transactional
    ShoppingCartDTO addToCart();

    ShoppingCartDTO addToCart(ShoppingCartDTO cartItem);

    void removeFromCart(Long userId);

    ShoppingCartDTO removeFromCart(ShoppingCartDTO cartItem);

    void removeFromCart(Long userId, Long productId);

    List<ShoppingCartDTO> getCartItems(Long userId);

    ShoppingCartItem updateCartItemQuantity(Long userId, Long productId, int quantity);

    void clearCart(Long userId);

    // Admin-specific methods
    ShoppingCartItem addToCart(Long productId, int quantity, BigDecimal price, Long adminId);

    ShoppingCartItem updateCartItemQuantity(Long cartItemId, int quantity);

    BigDecimal calculateCartTotal(Long adminId);
}
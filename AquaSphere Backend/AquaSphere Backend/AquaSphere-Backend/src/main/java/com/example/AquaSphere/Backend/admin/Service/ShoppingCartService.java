package com.example.AquaSphere.Backend.admin.Service;

import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import com.example.AquaSphere.Backend.User.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private Long id;
    private Long userId;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCart addToCart(Long productId, int quantity, BigDecimal price, Long adminId) {
        ShoppingCart cartItem = new ShoppingCart(id, userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);
        cartItem.setAdminId(adminId);
        cartItem.setAddedAt(LocalDateTime.now());
        return shoppingCartRepository.save(cartItem);
    }

    public List<ShoppingCart> getCartItems(Long adminId) {
        return shoppingCartRepository.findByAdminId(adminId);
    }

    @Transactional
    public ShoppingCart updateCartItemQuantity(Long cartItemId, int newQuantity) {
        return shoppingCartRepository.findById(cartItemId)
                .map(cartItem -> {
                    cartItem.setQuantity(newQuantity);
                    return shoppingCartRepository.save(cartItem);
                })
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
    }

    public void removeFromCart(Long cartItemId) {
        if (!shoppingCartRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart item not found");
        }
        shoppingCartRepository.deleteById(cartItemId);
    }

    @Transactional
    public void clearCart(Long adminId) {
        shoppingCartRepository.deleteByAdminId(adminId);
    }

    public BigDecimal calculateCartTotal(Long adminId) {
        List<ShoppingCart> cartItems = getCartItems(adminId);
        return cartItems.stream()
                .map(item -> {
                    BigDecimal price = Optional.ofNullable(item.getPrice()).orElse(BigDecimal.ZERO);
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
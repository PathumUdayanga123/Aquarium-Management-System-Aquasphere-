package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Repository;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity.CartItem;
import jakarta.persistence.ManyToOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}


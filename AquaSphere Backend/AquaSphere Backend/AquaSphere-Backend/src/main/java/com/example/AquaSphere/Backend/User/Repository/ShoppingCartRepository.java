package com.example.AquaSphere.Backend.User.Repository;

import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUserId(Long userId);
    List<ShoppingCart> findByAdminId(Long adminId);
    void deleteByUserId(Long userId);
    void deleteByAdminId(Long adminId);
    ShoppingCart findByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);
}
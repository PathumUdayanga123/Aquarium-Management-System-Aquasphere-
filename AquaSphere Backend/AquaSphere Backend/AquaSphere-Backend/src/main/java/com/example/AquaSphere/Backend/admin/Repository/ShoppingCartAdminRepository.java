package com.example.AquaSphere.Backend.admin.Repository;

import com.example.AquaSphere.Backend.admin.Entity.ShoppingCartAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartAdminRepository extends JpaRepository<ShoppingCartAdmin, Long> {
    List<ShoppingCartAdmin> findByAdminId(Long adminId);
    void deleteByAdminId(Long adminId);
    ShoppingCartAdmin findByAdminIdAndProductId(Long adminId, Long productId);
}

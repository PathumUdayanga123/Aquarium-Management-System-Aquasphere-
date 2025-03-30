package com.example.AquaSphere.Backend.admin.Entity;

import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("ADMIN")
public class ShoppingCartAdmin extends ShoppingCart {

    private static final Long userId =null;
    private static final Long id = null;

    public ShoppingCartAdmin() {
        super(id, userId);
    }

    public ShoppingCartAdmin(Long productId, int quantity, BigDecimal price, Long adminId) {
        super(id, userId);
        this.setProductId(productId);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setAdminId(adminId);
    }
}
package com.example.AquaSphere.Backend.admin.Entity;

import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("ADMIN")
public class ShoppingCartAdmin extends ShoppingCart {

    public ShoppingCartAdmin() {
        super();
    }

    public ShoppingCartAdmin(Long productId, int quantity, BigDecimal price, Long adminId) {
        super();
        this.setProductId(productId);
        this.setQuantity(quantity);
        this.setPrice(price);
        this.setAdminId(adminId);
    }
}
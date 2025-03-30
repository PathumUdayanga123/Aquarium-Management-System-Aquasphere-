package com.example.AquaSphere.Backend.admin.Controller;

import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import com.example.AquaSphere.Backend.admin.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/cart")
@Component("adminShoppingCartController")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public ResponseEntity<ShoppingCart> addToCart(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam BigDecimal price,
            @RequestParam Long adminId
    ){
        ShoppingCart cartItem = shoppingCartService.addToCart(productId, quantity, price, adminId);
        return ResponseEntity.ok(cartItem);
    }

    @GetMapping("/items/{adminId}")
    public ResponseEntity<List<ShoppingCart>> getCartItems(@PathVariable Long adminId){
        List<ShoppingCart> cartItems = shoppingCartService.getCartItems(adminId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ShoppingCart> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity
    ){
        ShoppingCart updateCartItem = shoppingCartService.updateCartItemQuantity(cartItemId, quantity);
        return ResponseEntity.ok(updateCartItem);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long cartItemId){
        shoppingCartService.removeFromCart(cartItemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear/{adminId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long adminId){
        shoppingCartService.clearCart(adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total/{adminId}")
    public ResponseEntity<BigDecimal> calculateCartTotal(@PathVariable Long adminId){
        BigDecimal total = shoppingCartService.calculateCartTotal(adminId);
        return ResponseEntity.ok(total);
    }
}
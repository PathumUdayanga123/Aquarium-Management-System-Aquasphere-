package com.example.AquaSphere.Backend.User.Controller;

import com.example.AquaSphere.Backend.User.DTO.ShoppingCartDTO;
import com.example.AquaSphere.Backend.User.Service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Component("adminShoppingCartController")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ShoppingCartDTO> addToCart(@RequestBody ShoppingCartDTO cartItem) {
        ShoppingCartDTO addedItem = cartService.addToCart(cartItem);
        return ResponseEntity.ok(addedItem);
    }

    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ){
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ShoppingCartDTO>> getCartItems(@PathVariable Long userId){
        List<ShoppingCartDTO> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/update/{userId}/{productId}")
    public ResponseEntity<Void> updateCartItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        cartService.updateCartItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId){
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
}
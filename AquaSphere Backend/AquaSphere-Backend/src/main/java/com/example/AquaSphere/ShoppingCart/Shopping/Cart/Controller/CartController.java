package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Controller;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.AddToCartDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.CartResponseDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart() {
        // In a real app, get userId from authenticated user
        Long userId = 1L; // Mock user ID for demonstration
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping
    public ResponseEntity<CartResponseDto> addToCart(@RequestBody AddToCartDto addToCartDto) {
        // In a real app, get userId from authenticated user
        Long userId = 1L; // Mock user ID for demonstration
        return ResponseEntity.ok(cartService.addToCart(userId, addToCartDto));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartResponseDto> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        // In a real app, get userId from authenticated user
        Long userId = 1L; // Mock user ID for demonstration
        return ResponseEntity.ok(cartService.updateCartItem(userId, itemId, quantity));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<CartResponseDto> removeFromCart(@PathVariable Long itemId) {
        // In a real app, get userId from authenticated user
        Long userId = 1L; // Mock user ID for demonstration
        return ResponseEntity.ok(cartService.removeFromCart(userId, itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        // In a real app, get userId from authenticated user
        Long userId = 1L; // Mock user ID for demonstration
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}


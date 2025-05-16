package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Controller;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.AddToCartDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.CartResponseDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // Returns the username
        }
        throw new SecurityException("User not authenticated");
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart() {
        String username = getAuthenticatedUsername();
        CartResponseDto cart = cartService.getCart(Long.valueOf(username));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(@Valid @RequestBody AddToCartDto addToCartDto) {
        String username = getAuthenticatedUsername();
        CartResponseDto updateCart = cartService.addToCart(Long.valueOf(username), addToCartDto);
        return ResponseEntity.ok(updateCart);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<CartResponseDto> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        String username = getAuthenticatedUsername();
        return ResponseEntity.ok(cartService.updateCartItem(Long.valueOf(username), itemId, quantity));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<CartResponseDto> removeFromCart(@PathVariable Long itemId) {
        String username = getAuthenticatedUsername();
        return ResponseEntity.ok(cartService.removeFromCart(Long.valueOf(username), itemId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        String username = getAuthenticatedUsername();
        cartService.clearCart(Long.valueOf(username));
        return ResponseEntity.noContent().build();
    }
}
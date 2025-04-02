package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.CartEntity;
import com.example.AquaSphere.Backend.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("Shopping_Cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartEntity addItemToCart(
            @RequestParam("itemCode") String itemCode,
            @RequestParam("itemName") String itemName,
            @RequestParam("itemPrice") double itemPrice,
            @RequestParam("quantity") int quantity,
            @RequestParam("itemPhoto") MultipartFile itemPhoto) {

        return cartService.addToCart(itemCode, itemName, itemPrice, quantity, itemPhoto);
    }

    @GetMapping("/get")
    public List<CartEntity> getAllCartItems() {
        return cartService.getAllCartItems();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }
}

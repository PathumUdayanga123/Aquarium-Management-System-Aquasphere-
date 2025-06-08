package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.CartDto;
import com.example.AquaSphere.Backend.Entity.CartEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CartService {
    CartEntity addToCart(String itemCode, String itemName, double itemPrice, int quantity, MultipartFile itemPhoto);
    void removeFromCart(Long cartId);
    List<CartEntity> getAllCartItems();
}

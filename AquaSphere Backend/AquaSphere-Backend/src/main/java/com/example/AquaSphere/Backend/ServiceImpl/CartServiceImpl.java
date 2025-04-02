package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.CartEntity;
import com.example.AquaSphere.Backend.Repository.CartRepository;
import com.example.AquaSphere.Backend.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public CartEntity addToCart(String itemCode, String itemName, double itemPrice, int quantity, MultipartFile itemPhoto) {
        CartEntity cartItem = new CartEntity();
        cartItem.setItemCode(itemCode);
        cartItem.setItemName(itemName);
        cartItem.setItemPrice(itemPrice);
        cartItem.setQuantity(quantity);
        cartItem.calculateTotalAmount(); // Calculate total price

        // Convert MultipartFile to byte[]
        try {
            cartItem.setItemPhoto(itemPhoto.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error processing image file", e);
        }

        return cartRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Override
    public List<CartEntity> getAllCartItems() {
        return cartRepository.findAll();
    }
}

package com.example.AquaSphere.Backend.User.ServiceImpl;

import com.example.AquaSphere.Backend.User.DTO.ShoppingCartDTO;
import com.example.AquaSphere.Backend.User.Entity.ShoppingCart;
import com.example.AquaSphere.Backend.User.Repository.ShoppingCartRepository;
import com.example.AquaSphere.Backend.User.Service.ShoppingCartService;
import com.example.AquaSphere.Backend.admin.Model.ShoppingCartItem;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository cartRepository;

    @Override
    @Transactional
    public ShoppingCartDTO addToCart() {
        final ShoppingCartDTO shoppingCartDTO;
        shoppingCartDTO = addToCart(null);
        return shoppingCartDTO;
    }

    @Override
    @Transactional
    public ShoppingCartDTO addToCart(ShoppingCartDTO cartItem) {
        // Check if item already exists in cart
        if (cartRepository.existsByUserIdAndProductId(cartItem.getUserId(), cartItem.getProductId())) {
            throw new RuntimeException("Product already in cart");
        }

        ShoppingCart cartEntity = new ShoppingCart();
        BeanUtils.copyProperties(cartItem, cartEntity);
        cartEntity.setAddedAt(LocalDateTime.now());

        ShoppingCart savedCart = cartRepository.save(cartEntity);

        ShoppingCartDTO savedDTO = new ShoppingCartDTO();
        BeanUtils.copyProperties(savedCart, savedDTO);
        return savedDTO;
    }

    @Override
    public void removeFromCart(Long userId) {

    }

    @Override
    public ShoppingCartDTO removeFromCart(ShoppingCartDTO cartItem) {
        return null;
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<ShoppingCartDTO> getCartItems(Long userId) {
        List<ShoppingCart> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream()
                .map(cart -> {
                    ShoppingCartDTO dto = new ShoppingCartDTO();
                    BeanUtils.copyProperties(cart, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShoppingCartItem updateCartItemQuantity(Long userId, Long productId, int quantity) {
        ShoppingCart cartItem = cartRepository.findByUserId(userId).stream()
                .filter(cart -> cart.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
        return null;
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        List<ShoppingCart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }

    //@Override
//    public ShoppingCartItem addToCart(Long productId, int quantity, BigDecimal price, Long adminId) {
//        return null;
   // }

    @Override
    public BigDecimal calculateCartTotal(Long adminId) {
        return null;
    }

//    @Override
//    public ShoppingCartItem addToCart(Long productId, int quantity, BigDecimal price, Long adminId) {
//        return null;
//    }
//
//    @Override
//    public ShoppingCartItem updateCartItemQuantity(Long cartItemId, int quantity) {
//        return null;
//    }
//
//    @Override
//    public BigDecimal calculateCartTotal(Long adminId) {
//        return null;
//    }
}

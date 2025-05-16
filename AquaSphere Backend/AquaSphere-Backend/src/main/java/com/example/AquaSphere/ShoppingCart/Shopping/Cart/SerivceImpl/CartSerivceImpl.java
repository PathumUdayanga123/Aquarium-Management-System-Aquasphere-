package com.example.aquaSphere.ShoppingCart.Shopping.Cart.SerivceImpl;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.AddToCartDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.CartItemDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.DTO.CartResponseDto;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity.CartItem;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity.Product;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Repository.CartItemRepository;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Repository.ProductRepository;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartSerivceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartSerivceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public CartResponseDto getCart(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        return buildCartResponse(cartItems);
    }

    @Override
    @Transactional
    public CartResponseDto addToCart(Long userId, AddToCartDto addToCartDto) {
        // Validate product exists
        Product product = productRepository.findById(addToCartDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product is in stock
        if (product.getStockQuantity() < addToCartDto.getQuantity()) {
            throw new RuntimeException("Not enough items in stock");
        }

        // Check if the item already exists in cart
        Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(userId, product.getId());

        CartItem item;
        if (existingItem.isPresent()) {
            // Update quantity
            item = existingItem.get();
            item.setQuantity(item.getQuantity() + addToCartDto.getQuantity());
        } else {
            // Create new cart item
            item = new CartItem();
            item.setUserId(userId);
            item.setProduct(product);
            item.setQuantity(addToCartDto.getQuantity());
            item.setitemType(userId.toString());
            item.setItemName(product.getName());
            item.setItemAddDate(LocalDateTime.now());
            item.setQuantity(addToCartDto.getQuantity());
            item.setItemCode(product.getId().toString());
            item.setItemDescription(product.getDescription());
            item.setItemPrice(product.getPrice());
            item.setDiscount(0.0);
        }
        cartItemRepository.save(item);

        // Return updated cart
        return getCart(userId);
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItem(Long userId, Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!Objects.equals(item.getUserId(), userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        // Handle deletion if quantity is 0
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            // Verify stock before updating
            if (item.getProduct().getStockQuantity() < quantity) {
                throw new RuntimeException("Not enough items in stock");
            }

            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return getCart(userId);
    }

    @Override
    @Transactional
    public CartResponseDto removeFromCart(Long userId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to cart item");
        }

        cartItemRepository.delete(item);
        return getCart(userId);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

    private CartResponseDto buildCartResponse(List<CartItem> cartItems) {
        if (cartItems.isEmpty()) {
            return new CartResponseDto(new ArrayList<>(), 0.0, 0);
        }

        // Get all product IDs from cart items
        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    return new CartItemDto(
                            item.getId(),
                            product.getId(),
                            product.getName(),
                            product.getImageUrl(),
                            item.getQuantity(),
                            product.getPrice(),
                            item.getQuantity() * product.getPrice()
                    );
                })
                .collect(Collectors.toList());

        // Fetch all products in one query
        Double total = cartItemDtos.stream()
                .mapToDouble(CartItemDto::getSubtotal)
                .sum();
        Integer itemCount = cartItemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
        return new CartResponseDto(cartItemDtos, total, itemCount);
    }
}
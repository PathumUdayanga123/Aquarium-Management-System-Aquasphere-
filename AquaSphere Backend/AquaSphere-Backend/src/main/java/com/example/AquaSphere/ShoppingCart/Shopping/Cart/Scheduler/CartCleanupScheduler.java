package com.example.aquaSphere.ShoppingCart.Shopping.Cart.Scheduler;

import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Entity.CartItem;
import com.example.aquaSphere.ShoppingCart.Shopping.Cart.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartCleanupScheduler {

    private final CartItemRepository cartItemRepository;

    @Autowired
    public CartCleanupScheduler(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    // Run at midnight every day
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupAbandonedCarts() {
        // Find cart items older than 30 days
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        List<CartItem> oldItems = cartItemRepository.findAll().stream()
                .filter(item -> item.getUpdatedAt().isBefore(thirtyDaysAgo))
                .collect(Collectors.toList());

        if (!oldItems.isEmpty()) {
            cartItemRepository.deleteAll(oldItems);
            System.out.println("Cleaned up " + oldItems.size() + " abandoned cart items");
        }
    }
}

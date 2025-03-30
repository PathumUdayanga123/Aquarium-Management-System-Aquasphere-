package com.example.AquaSphere.Backend.User.Sheduler;

import com.example.AquaSphere.Backend.User.Repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CartCleanupScheduler {

    @Autowired
    private ShoppingCartRepository cartRepository; // Changed from ShoppingCartAdminRepository

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void cleanupOldCartItems() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        cartRepository.findAll().stream()
                .filter(cart -> cart.getAddedAt().isBefore(thirtyDaysAgo))
                .forEach(cartRepository::delete);
    }

}
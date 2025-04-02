package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.InventoryDto;

import com.example.AquaSphere.Backend.Entity.InventoryEntity;
import com.example.AquaSphere.Backend.Entity.ReviewEntity;


import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<InventoryEntity> getAllItems(); // Get all items
    List<InventoryEntity> getItemsByCategory(String itemType); // Get items by category using logic

    ;

    // Search items by name
    InventoryEntity addItem(InventoryDto inventoryDto); // Add item
    InventoryEntity updateItem(String itemCode, InventoryDto inventoryDto); // Update item
    void deleteItem(String itemCode); // Delete item

    List<InventoryEntity> searchItemsByDescription(String prefix);

    Optional<InventoryEntity> getItemById(Long id); // Get item by ID







}

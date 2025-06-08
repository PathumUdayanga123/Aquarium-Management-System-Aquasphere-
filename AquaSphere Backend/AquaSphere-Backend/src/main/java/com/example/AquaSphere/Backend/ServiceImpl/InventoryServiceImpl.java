package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.DTO.InventoryDto;
import com.example.AquaSphere.Backend.Entity.InventoryEntity;

import com.example.AquaSphere.Backend.Repository.InventoryRepository;
import com.example.AquaSphere.Backend.Service.InventoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<InventoryEntity> getAllItems() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<InventoryEntity> getItemsByCategory(String itemType) {
        return inventoryRepository.findAll()
                .stream()
                .filter(item -> item.getItemType().equalsIgnoreCase(itemType))
                .collect(Collectors.toList());
    }

    @Override
    public List<InventoryEntity> searchItemsByDescription(String prefix) {
        return inventoryRepository.searchByItemDescriptionPrefix(prefix);
    }

    @Override
    public InventoryEntity addItem(InventoryDto inventoryDto) {
        InventoryEntity item = new InventoryEntity();
        item.setItemType(inventoryDto.getItemType());
        item.setItemName(inventoryDto.getItemName());
        item.setItemAddDate(LocalDate.now());
        item.setItemCode(inventoryDto.getItemCode());
        item.setItemDescription(inventoryDto.getItemDescription());
        item.setItemPrice(inventoryDto.getItemPrice());
        item.setInitialCost(inventoryDto.getInitialCost()); // Set initial cost
        item.setStockCount(inventoryDto.getStockCount());
        item.setDiscount(inventoryDto.getDiscount());

        if (inventoryDto.getItemPhoto() != null) {
            item.setItemPhoto(inventoryDto.getItemPhoto());
        }

        item.calculateTotalAmount(); // Calculate total amount after discount
        item.calculateProfit(); // Calculate profit
        item.updateStockStatus(); // Update stock status
        return inventoryRepository.save(item);
    }

    @Override
    public InventoryEntity updateItem(String itemCode, InventoryDto inventoryDto) {
        Optional<InventoryEntity> existingItem = inventoryRepository.findByItemCode(itemCode);

        if (existingItem.isPresent()) {
            InventoryEntity item = existingItem.get();
            item.setItemType(inventoryDto.getItemType());
            item.setItemName(inventoryDto.getItemName());
            item.setItemDescription(inventoryDto.getItemDescription());
            item.setItemPrice(inventoryDto.getItemPrice());
            item.setInitialCost(inventoryDto.getInitialCost()); // Update initial cost
            item.setStockCount(inventoryDto.getStockCount());
            item.setDiscount(inventoryDto.getDiscount());

            if (inventoryDto.getItemPhoto() != null) {
                item.setItemPhoto(inventoryDto.getItemPhoto());
            }

            item.calculateTotalAmount(); // Recalculate total amount
            item.calculateProfit(); // Recalculate profit
            item.updateStockStatus(); // Update stock status
            return inventoryRepository.save(item);
        }
        return null;
    }

    @Override
    public void deleteItem(String itemCode) {
        Optional<InventoryEntity> item = inventoryRepository.findByItemCode(itemCode);
        item.ifPresent(inventoryRepository::delete);
    }

    @Override
    public Optional<InventoryEntity> getItemById(Long id) {
        return inventoryRepository.findById(id);
    }
}
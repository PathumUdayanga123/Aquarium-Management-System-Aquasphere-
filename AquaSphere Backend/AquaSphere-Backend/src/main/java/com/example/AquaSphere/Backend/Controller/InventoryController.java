package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.InventoryDto;
import com.example.AquaSphere.Backend.Entity.InventoryEntity;

import com.example.AquaSphere.Backend.Service.InventoryService;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/Fish")
    public ResponseEntity<List<InventoryEntity>> getFishItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Fish"));
    }

    @GetMapping("/Filter")
    public ResponseEntity<List<InventoryEntity>> getFilterItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Filter"));
    }

    @GetMapping("/Food")
    public ResponseEntity<List<InventoryEntity>> getFoodItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Food"));
    }

    @GetMapping("/Shrimp")
    public ResponseEntity<List<InventoryEntity>> getShrimpItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Shrimp"));
    }

    @GetMapping("/Accessorie")
    public ResponseEntity<List<InventoryEntity>> getAccessoriesItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Accessorie"));
    }

    @GetMapping("/Medicine")
    public ResponseEntity<List<InventoryEntity>> getMedicineItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Medicine"));
    }

    @GetMapping("/Plant")
    public ResponseEntity<List<InventoryEntity>> getPlantsItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Plant"));
    }

    @GetMapping("/Tank")
    public ResponseEntity<List<InventoryEntity>> getTankItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Tank"));
    }

    @GetMapping("/Certified_Fish")
    public ResponseEntity<List<InventoryEntity>> getCertified_FishItems() {
        return ResponseEntity.ok(inventoryService.getItemsByCategory("Certified_Fish"));
    }

    @GetMapping("/search/{prefix}")
    public ResponseEntity<List<InventoryEntity>> searchItems(@PathVariable String prefix) {
        return ResponseEntity.ok(inventoryService.searchItemsByDescription(prefix));
    }




    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InventoryEntity> addItem(
            @RequestParam String itemType,
            @RequestParam String itemName,
            @RequestParam String itemCode,
            @RequestParam String itemDescription,
            @RequestParam double itemPrice,
            @RequestParam int stockCount,
            @RequestParam double discount,
            @RequestParam(required = false) MultipartFile itemPhoto) throws IOException {

        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setItemType(itemType);
        inventoryDto.setItemName(itemName);
        inventoryDto.setItemAddDate(LocalDate.now());
        inventoryDto.setItemCode(itemCode);
        inventoryDto.setItemDescription(itemDescription);
        inventoryDto.setItemPrice(itemPrice);
        inventoryDto.setStockCount(stockCount);
        inventoryDto.setDiscount(discount);

        if (itemPhoto != null && !itemPhoto.isEmpty()) {
            inventoryDto.setItemPhoto(itemPhoto.getBytes());
        }

        // Save the item to the database and update stock status
        InventoryEntity newItem = inventoryService.addItem(inventoryDto);
        newItem.updateStockStatus(); // Update stock status after item is added
        return ResponseEntity.ok(newItem);
    }

    @PutMapping("/update/{itemCode}")
    public ResponseEntity<InventoryEntity> updateItem(
            @PathVariable String itemCode,
            @RequestBody InventoryDto inventoryDto) {
        InventoryEntity updatedItem = inventoryService.updateItem(itemCode, inventoryDto);
        updatedItem.updateStockStatus(); // Update stock status after update
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{itemCode}")
    public ResponseEntity<Void> deleteItem(@PathVariable String itemCode) {
        inventoryService.deleteItem(itemCode);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/viewallitems")
    public List<InventoryEntity> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryEntity> getItemById(@PathVariable Long id) {
        return inventoryService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

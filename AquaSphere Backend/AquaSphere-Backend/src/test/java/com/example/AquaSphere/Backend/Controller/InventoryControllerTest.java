package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.InventoryDto;
import com.example.AquaSphere.Backend.Entity.InventoryEntity;
import com.example.AquaSphere.Backend.Service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private List<InventoryEntity> sampleInventory;
    private InventoryEntity sampleEntity;
    private InventoryDto sampleDto;

    @BeforeEach
    void setUp() {
        // Initialize sample data for tests
        sampleInventory = new ArrayList<>();

        sampleEntity = new InventoryEntity();
        sampleEntity.setId(1L);
        sampleEntity.setItemCode("FISH001");
        sampleEntity.setItemName("Goldfish");
        sampleEntity.setItemType("Fish");
        sampleEntity.setItemDescription("Common goldfish");
        sampleEntity.setItemPrice(10.99);
        sampleEntity.setInitialCost(5.50);
        sampleEntity.setStockCount(20);
        sampleEntity.setDiscount(0.0);
        sampleEntity.setItemAddDate(LocalDate.now());

        sampleInventory.add(sampleEntity);

        sampleDto = new InventoryDto();
        sampleDto.setItemCode("FISH001");
        sampleDto.setItemName("Goldfish");
        sampleDto.setItemType("Fish");
        sampleDto.setItemDescription("Common goldfish");
        sampleDto.setItemPrice(10.99);
        sampleDto.setInitialCost(5.50);
        sampleDto.setStockCount(20);
        sampleDto.setDiscount(0.0);
        sampleDto.setItemAddDate(LocalDate.now());
    }

    @Test
    void getFishItems_ShouldReturnFishInventory() {
        // Arrange
        when(inventoryService.getItemsByCategory("Fish")).thenReturn(sampleInventory);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getFishItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleInventory, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Fish");
    }

    @Test
    void getFilterItems_ShouldReturnFilterInventory() {
        // Arrange
        List<InventoryEntity> filterItems = new ArrayList<>();
        InventoryEntity filterItem = new InventoryEntity();
        filterItem.setItemType("Filter");
        filterItems.add(filterItem);

        when(inventoryService.getItemsByCategory("Filter")).thenReturn(filterItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getFilterItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(filterItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Filter");
    }

    @Test
    void getFoodItems_ShouldReturnFoodInventory() {
        // Arrange
        List<InventoryEntity> foodItems = new ArrayList<>();
        InventoryEntity foodItem = new InventoryEntity();
        foodItem.setItemType("Food");
        foodItems.add(foodItem);

        when(inventoryService.getItemsByCategory("Food")).thenReturn(foodItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getFoodItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(foodItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Food");
    }

    @Test
    void getShrimpItems_ShouldReturnShrimpInventory() {
        // Arrange
        List<InventoryEntity> shrimpItems = new ArrayList<>();
        InventoryEntity shrimpItem = new InventoryEntity();
        shrimpItem.setItemType("Shrimp");
        shrimpItems.add(shrimpItem);

        when(inventoryService.getItemsByCategory("Shrimp")).thenReturn(shrimpItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getShrimpItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(shrimpItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Shrimp");
    }

    @Test
    void getAccessoriesItems_ShouldReturnAccessorieInventory() {
        // Arrange
        List<InventoryEntity> accessorieItems = new ArrayList<>();
        InventoryEntity accessorieItem = new InventoryEntity();
        accessorieItem.setItemType("Accessorie");
        accessorieItems.add(accessorieItem);

        when(inventoryService.getItemsByCategory("Accessorie")).thenReturn(accessorieItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getAccessoriesItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(accessorieItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Accessorie");
    }

    @Test
    void getMedicineItems_ShouldReturnMedicineInventory() {
        // Arrange
        List<InventoryEntity> medicineItems = new ArrayList<>();
        InventoryEntity medicineItem = new InventoryEntity();
        medicineItem.setItemType("Medicine");
        medicineItems.add(medicineItem);

        when(inventoryService.getItemsByCategory("Medicine")).thenReturn(medicineItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getMedicineItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(medicineItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Medicine");
    }

    @Test
    void getPlantsItems_ShouldReturnPlantInventory() {
        // Arrange
        List<InventoryEntity> plantItems = new ArrayList<>();
        InventoryEntity plantItem = new InventoryEntity();
        plantItem.setItemType("Plant");
        plantItems.add(plantItem);

        when(inventoryService.getItemsByCategory("Plant")).thenReturn(plantItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getPlantsItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(plantItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Plant");
    }

    @Test
    void getTankItems_ShouldReturnTankInventory() {
        // Arrange
        List<InventoryEntity> tankItems = new ArrayList<>();
        InventoryEntity tankItem = new InventoryEntity();
        tankItem.setItemType("Tank");
        tankItems.add(tankItem);

        when(inventoryService.getItemsByCategory("Tank")).thenReturn(tankItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getTankItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(tankItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Tank");
    }

    @Test
    void getCertified_FishItems_ShouldReturnCertifiedFishInventory() {
        // Arrange
        List<InventoryEntity> certifiedFishItems = new ArrayList<>();
        InventoryEntity certifiedFishItem = new InventoryEntity();
        certifiedFishItem.setItemType("Certified_Fish");
        certifiedFishItems.add(certifiedFishItem);

        when(inventoryService.getItemsByCategory("Certified_Fish")).thenReturn(certifiedFishItems);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.getCertified_FishItems();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(certifiedFishItems, response.getBody());
        verify(inventoryService, times(1)).getItemsByCategory("Certified_Fish");
    }

    @Test
    void searchItems_ShouldReturnMatchingItems() {
        // Arrange
        String searchPrefix = "gold";
        List<InventoryEntity> searchResults = new ArrayList<>();
        searchResults.add(sampleEntity);

        when(inventoryService.searchItemsByDescription(searchPrefix)).thenReturn(searchResults);

        // Act
        ResponseEntity<List<InventoryEntity>> response = inventoryController.searchItems(searchPrefix);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(searchResults, response.getBody());
        verify(inventoryService, times(1)).searchItemsByDescription(searchPrefix);
    }

    @Test
    void addItem_ShouldCreateNewInventoryItem() throws IOException {
        // Arrange
        MockMultipartFile itemPhoto = new MockMultipartFile(
                "itemPhoto",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        when(inventoryService.addItem(any(InventoryDto.class))).thenReturn(sampleEntity);

        // Act
        ResponseEntity<InventoryEntity> response = inventoryController.addItem(
                "Fish",
                "Goldfish",
                "FISH001",
                "Common goldfish",
                10.99,
                5.50,
                20,
                0.0,
                itemPhoto
        );

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleEntity, response.getBody());
        verify(inventoryService, times(1)).addItem(any(InventoryDto.class));
    }

    @Test
    void updateItem_ShouldUpdateExistingItem() {
        // Arrange
        String itemCode = "FISH001";
        InventoryEntity updatedEntity = new InventoryEntity();
        updatedEntity.setItemName("Updated Goldfish");

        when(inventoryService.updateItem(eq(itemCode), any(InventoryDto.class))).thenReturn(updatedEntity);

        // Act
        ResponseEntity<InventoryEntity> response = inventoryController.updateItem(itemCode, sampleDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedEntity, response.getBody());
        verify(inventoryService, times(1)).updateItem(eq(itemCode), any(InventoryDto.class));
    }

    @Test
    void deleteItem_ShouldDeleteItemAndReturnNoContent() {
        // Arrange
        String itemCode = "FISH001";
        doNothing().when(inventoryService).deleteItem(itemCode);

        // Act
        ResponseEntity<Void> response = inventoryController.deleteItem(itemCode);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(inventoryService, times(1)).deleteItem(itemCode);
    }

    @Test
    void getAllItems_ShouldReturnAllInventoryItems() {
        // Arrange
        when(inventoryService.getAllItems()).thenReturn(sampleInventory);

        // Act
        List<InventoryEntity> result = inventoryController.getAllItems();

        // Assert
        assertEquals(sampleInventory, result);
        verify(inventoryService, times(1)).getAllItems();
    }

    @Test
    void getItemById_ShouldReturnItemWhenExists() {
        // Arrange
        Long itemId = 1L;
        when(inventoryService.getItemById(itemId)).thenReturn(Optional.of(sampleEntity));

        // Act
        ResponseEntity<InventoryEntity> response = inventoryController.getItemById(itemId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleEntity, response.getBody());
        verify(inventoryService, times(1)).getItemById(itemId);
    }

    @Test
    void getItemById_ShouldReturnNotFoundWhenItemDoesNotExist() {
        // Arrange
        Long itemId = 999L;
        when(inventoryService.getItemById(itemId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<InventoryEntity> response = inventoryController.getItemById(itemId);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(inventoryService, times(1)).getItemById(itemId);
    }
}
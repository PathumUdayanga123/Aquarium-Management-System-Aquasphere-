package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.DTO.InventoryDto;
import com.example.AquaSphere.Backend.Entity.InventoryEntity;
import com.example.AquaSphere.Backend.Repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private List<InventoryEntity> inventoryList;
    private InventoryEntity fishItem;
    private InventoryEntity plantItem;
    private InventoryDto inventoryDto;

    @BeforeEach
    void setUp() {
        // Initialize test data
        inventoryList = new ArrayList<>();

        // Create a sample fish item
        fishItem = new InventoryEntity();
        fishItem.setId(1L);
        fishItem.setItemCode("FISH001");
        fishItem.setItemName("Goldfish");
        fishItem.setItemType("Fish");
        fishItem.setItemDescription("Common goldfish");
        fishItem.setItemPrice(10.99);
        fishItem.setInitialCost(5.50);
        fishItem.setStockCount(20);
        fishItem.setDiscount(0.0);
        fishItem.setItemAddDate(LocalDate.now());

        // Create a sample plant item
        plantItem = new InventoryEntity();
        plantItem.setId(2L);
        plantItem.setItemCode("PLANT001");
        plantItem.setItemName("Java Fern");
        plantItem.setItemType("Plant");
        plantItem.setItemDescription("Easy to grow aquarium plant");
        plantItem.setItemPrice(6.99);
        plantItem.setInitialCost(3.50);
        plantItem.setStockCount(15);
        plantItem.setDiscount(0.0);
        plantItem.setItemAddDate(LocalDate.now());

        // Add both items to the list
        inventoryList.add(fishItem);
        inventoryList.add(plantItem);

        // Create a DTO for testing add/update operations
        inventoryDto = new InventoryDto();
        inventoryDto.setItemCode("FISH002");
        inventoryDto.setItemName("Betta Fish");
        inventoryDto.setItemType("Fish");
        inventoryDto.setItemDescription("Siamese fighting fish");
        inventoryDto.setItemPrice(15.99);
        inventoryDto.setInitialCost(8.0);
        inventoryDto.setStockCount(10);
        inventoryDto.setDiscount(0.0);
        inventoryDto.setItemAddDate(LocalDate.now());
    }

    @Test
    void getAllItems_ShouldReturnAllInventoryItems() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // Act
        List<InventoryEntity> result = inventoryService.getAllItems();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(fishItem));
        assertTrue(result.contains(plantItem));
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void getItemsByCategory_ShouldReturnItemsOfSpecificCategory() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // Act
        List<InventoryEntity> fishItems = inventoryService.getItemsByCategory("Fish");
        List<InventoryEntity> plantItems = inventoryService.getItemsByCategory("Plant");
        List<InventoryEntity> filterItems = inventoryService.getItemsByCategory("Filter");

        // Assert
        assertEquals(1, fishItems.size());
        assertEquals("Goldfish", fishItems.get(0).getItemName());

        assertEquals(1, plantItems.size());
        assertEquals("Java Fern", plantItems.get(0).getItemName());

        assertEquals(0, filterItems.size());

        verify(inventoryRepository, times(3)).findAll();
    }

    @Test
    void getItemsByCategory_ShouldBeCaseInsensitive() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(inventoryList);

        // Act
        List<InventoryEntity> result = inventoryService.getItemsByCategory("fish");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Goldfish", result.get(0).getItemName());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void searchItemsByDescription_ShouldReturnMatchingItems() {
        // Arrange
        String searchPrefix = "gold";
        List<InventoryEntity> matchingItems = new ArrayList<>();
        matchingItems.add(fishItem);

        when(inventoryRepository.searchByItemDescriptionPrefix(searchPrefix)).thenReturn(matchingItems);

        // Act
        List<InventoryEntity> result = inventoryService.searchItemsByDescription(searchPrefix);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Goldfish", result.get(0).getItemName());
        verify(inventoryRepository, times(1)).searchByItemDescriptionPrefix(searchPrefix);
    }

    @Test
    void addItem_ShouldCreateAndReturnNewItem() {
        // Arrange
        InventoryEntity newItem = new InventoryEntity();
        newItem.setItemType(inventoryDto.getItemType());
        newItem.setItemName(inventoryDto.getItemName());
        newItem.setItemCode(inventoryDto.getItemCode());
        newItem.setItemDescription(inventoryDto.getItemDescription());
        newItem.setItemPrice(inventoryDto.getItemPrice());
        newItem.setInitialCost(inventoryDto.getInitialCost());
        newItem.setStockCount(inventoryDto.getStockCount());
        newItem.setDiscount(inventoryDto.getDiscount());
        newItem.setItemAddDate(LocalDate.now());

        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(newItem);

        // Act
        InventoryEntity result = inventoryService.addItem(inventoryDto);

        // Assert
        assertNotNull(result);
        assertEquals("Betta Fish", result.getItemName());
        assertEquals("FISH002", result.getItemCode());
        verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
    }

    @Test
    void updateItem_ShouldUpdateExistingItem() {
        // Arrange
        String itemCode = "FISH001";
        InventoryEntity updatedItem = new InventoryEntity();
        updatedItem.setId(1L);
        updatedItem.setItemCode(itemCode);
        updatedItem.setItemName("Updated Goldfish");
        updatedItem.setItemType("Fish");
        updatedItem.setItemDescription("Updated description");
        updatedItem.setItemPrice(12.99);
        updatedItem.setInitialCost(6.50);
        updatedItem.setStockCount(25);
        updatedItem.setDiscount(0.10);

        InventoryDto updateDto = new InventoryDto();
        updateDto.setItemName("Updated Goldfish");
        updateDto.setItemType("Fish");
        updateDto.setItemDescription("Updated description");
        updateDto.setItemPrice(12.99);
        updateDto.setInitialCost(6.50);
        updateDto.setStockCount(25);
        updateDto.setDiscount(0.10);

        when(inventoryRepository.findByItemCode(itemCode)).thenReturn(Optional.of(fishItem));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(updatedItem);

        // Act
        InventoryEntity result = inventoryService.updateItem(itemCode, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Goldfish", result.getItemName());
        assertEquals(12.99, result.getItemPrice());
        assertEquals(25, result.getStockCount());
        assertEquals(0.10, result.getDiscount());
        verify(inventoryRepository, times(1)).findByItemCode(itemCode);
        verify(inventoryRepository, times(1)).save(any(InventoryEntity.class));
    }

    @Test
    void updateItem_ShouldReturnNullWhenItemNotFound() {
        // Arrange
        String nonExistentItemCode = "NONEXISTENT";

        when(inventoryRepository.findByItemCode(nonExistentItemCode)).thenReturn(Optional.empty());

        // Act
        InventoryEntity result = inventoryService.updateItem(nonExistentItemCode, inventoryDto);

        // Assert
        assertNull(result);
        verify(inventoryRepository, times(1)).findByItemCode(nonExistentItemCode);
        verify(inventoryRepository, never()).save(any(InventoryEntity.class));
    }

    @Test
    void deleteItem_ShouldCallRepositoryDelete() {
        // Arrange
        String itemCode = "FISH001";

        when(inventoryRepository.findByItemCode(itemCode)).thenReturn(Optional.of(fishItem));

        // Act
        inventoryService.deleteItem(itemCode);

        // Assert
        verify(inventoryRepository, times(1)).findByItemCode(itemCode);
        verify(inventoryRepository, times(1)).delete(fishItem);
    }

    @Test
    void deleteItem_ShouldDoNothingWhenItemNotFound() {
        // Arrange
        String nonExistentItemCode = "NONEXISTENT";

        when(inventoryRepository.findByItemCode(nonExistentItemCode)).thenReturn(Optional.empty());

        // Act
        inventoryService.deleteItem(nonExistentItemCode);

        // Assert
        verify(inventoryRepository, times(1)).findByItemCode(nonExistentItemCode);
        verify(inventoryRepository, never()).delete(any(InventoryEntity.class));
    }

    @Test
    void getItemById_ShouldReturnItemWhenExists() {
        // Arrange
        Long itemId = 1L;

        when(inventoryRepository.findById(itemId)).thenReturn(Optional.of(fishItem));

        // Act
        Optional<InventoryEntity> result = inventoryService.getItemById(itemId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Goldfish", result.get().getItemName());
        verify(inventoryRepository, times(1)).findById(itemId);
    }

    @Test
    void getItemById_ShouldReturnEmptyOptionalWhenItemNotFound() {
        // Arrange
        Long nonExistentItemId = 999L;

        when(inventoryRepository.findById(nonExistentItemId)).thenReturn(Optional.empty());

        // Act
        Optional<InventoryEntity> result = inventoryService.getItemById(nonExistentItemId);

        // Assert
        assertFalse(result.isPresent());
        verify(inventoryRepository, times(1)).findById(nonExistentItemId);
    }
}
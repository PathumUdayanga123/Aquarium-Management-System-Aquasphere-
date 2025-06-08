package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.CartEntity;
import com.example.AquaSphere.Backend.Service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartControllerTest {

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    private CartEntity mockCartItem;
    private MultipartFile mockItemPhoto;

    @BeforeEach
    void setUp() {
        // Setup mock data
        mockCartItem = new CartEntity();
        mockCartItem.setId(1L);
        mockCartItem.setItemCode("FISH001");
        mockCartItem.setItemName("Goldfish");
        mockCartItem.setItemPrice(15.99);
        mockCartItem.setQuantity(2);
        // For a real cart entity, you would call calculateTotalAmount()

        // Create a mock MultipartFile
        mockItemPhoto = new MockMultipartFile(
                "itemPhoto",
                "goldfish.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void addItemToCart_shouldReturnAddedItem() {
        // Arrange
        when(cartService.addToCart(anyString(), anyString(), anyDouble(), anyInt(), any(MultipartFile.class)))
                .thenReturn(mockCartItem);

        // Act
        CartEntity result = cartController.addItemToCart(
                "FISH001",
                "Goldfish",
                15.99,
                2,
                mockItemPhoto
        );

        // Assert
        assertNotNull(result);
        assertEquals(mockCartItem.getId(), result.getId());
        assertEquals(mockCartItem.getItemCode(), result.getItemCode());
        assertEquals(mockCartItem.getItemName(), result.getItemName());
        assertEquals(mockCartItem.getItemPrice(), result.getItemPrice());
        assertEquals(mockCartItem.getQuantity(), result.getQuantity());

        // Verify service method was called with correct parameters
        verify(cartService).addToCart(
                eq("FISH001"),
                eq("Goldfish"),
                eq(15.99),
                eq(2),
                eq(mockItemPhoto)
        );
    }

    @Test
    void getAllCartItems_shouldReturnAllItems() {
        // Arrange
        CartEntity secondItem = new CartEntity();
        secondItem.setId(2L);
        secondItem.setItemCode("TANK001");
        secondItem.setItemName("10 Gallon Tank");
        secondItem.setItemPrice(49.99);
        secondItem.setQuantity(1);

        List<CartEntity> mockCartItems = Arrays.asList(mockCartItem, secondItem);
        when(cartService.getAllCartItems()).thenReturn(mockCartItems);

        // Act
        List<CartEntity> result = cartController.getAllCartItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("FISH001", result.get(0).getItemCode());
        assertEquals("TANK001", result.get(1).getItemCode());

        // Verify service method was called
        verify(cartService).getAllCartItems();
    }

    @Test
    void deleteItem_shouldReturnNoContent() {
        // Arrange
        Long itemId = 1L;
        doNothing().when(cartService).removeFromCart(itemId);

        // Act
        ResponseEntity<Void> response = cartController.deleteItem(itemId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify service method was called with correct ID
        verify(cartService).removeFromCart(itemId);
    }
}
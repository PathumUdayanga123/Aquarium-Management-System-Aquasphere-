package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.CartEntity;
import com.example.AquaSphere.Backend.Repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartEntity mockCartItem;
    private MultipartFile mockItemPhoto;

    @BeforeEach
    void setUp() {
        // Create mock cart item
        mockCartItem = new CartEntity();
        mockCartItem.setId(1L);
        mockCartItem.setItemCode("FISH001");
        mockCartItem.setItemName("Goldfish");
        mockCartItem.setItemPrice(15.99);
        mockCartItem.setQuantity(2);
        // Total would be calculated in a real entity

        // Create mock MultipartFile
        mockItemPhoto = new MockMultipartFile(
                "itemPhoto",
                "goldfish.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
    }

    @Test
    void addToCart_shouldSaveAndReturnCartEntity() throws IOException {
        // Arrange
        when(cartRepository.save(any(CartEntity.class))).thenAnswer(invocation -> {
            CartEntity savedItem = invocation.getArgument(0);
            savedItem.setId(1L); // Simulate database assigning ID
            return savedItem;
        });

        // Act
        CartEntity result = cartService.addToCart(
                "FISH001",
                "Goldfish",
                15.99,
                2,
                mockItemPhoto
        );

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("FISH001", result.getItemCode());
        assertEquals("Goldfish", result.getItemName());
        assertEquals(15.99, result.getItemPrice());
        assertEquals(2, result.getQuantity());
        assertArrayEquals(mockItemPhoto.getBytes(), result.getItemPhoto());

        // Verify repository was called to save the entity
        verify(cartRepository).save(any(CartEntity.class));
    }

    @Test
    void addToCart_shouldThrowExceptionOnIOError() throws IOException {
        // Arrange
        MultipartFile mockErrorFile = mock(MultipartFile.class);
        when(mockErrorFile.getBytes()).thenThrow(new IOException("Test IO exception"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            cartService.addToCart("TEST001", "Test Item", 10.0, 1, mockErrorFile);
        });

        assertTrue(exception.getMessage().contains("Error processing image file"));
        verify(cartRepository, never()).save(any(CartEntity.class));
    }

    @Test
    void removeFromCart_shouldDeleteCartItem() {
        // Arrange
        Long itemId = 1L;
        doNothing().when(cartRepository).deleteById(itemId);

        // Act
        cartService.removeFromCart(itemId);

        // Assert - verify repository method was called with correct ID
        verify(cartRepository).deleteById(itemId);
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
        when(cartRepository.findAll()).thenReturn(mockCartItems);

        // Act
        List<CartEntity> result = cartService.getAllCartItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("FISH001", result.get(0).getItemCode());
        assertEquals("TANK001", result.get(1).getItemCode());

        // Verify repository method was called
        verify(cartRepository).findAll();
    }
}
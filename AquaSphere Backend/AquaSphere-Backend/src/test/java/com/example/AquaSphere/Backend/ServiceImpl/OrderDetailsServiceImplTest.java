package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.DTO.OrderDetailsDTO;
import com.example.AquaSphere.Backend.Entity.OrderDetails;
import com.example.AquaSphere.Backend.Repository.OrderDetailsRepository;
import com.example.AquaSphere.Backend.Service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailsServiceImplTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrderDetailsServiceImpl orderDetailsService;

    @Captor
    private ArgumentCaptor<OrderDetails> orderDetailsCaptor;

    @Captor
    private ArgumentCaptor<String> emailCaptor;

    @Captor
    private ArgumentCaptor<String> subjectCaptor;

    @Captor
    private ArgumentCaptor<String> bodyCaptor;

    private OrderDetailsDTO validOrderDTO;
    private OrderDetails mockOrder;
    private List<OrderDetails> mockOrdersList;

    @BeforeEach
    void setUp() {
        // Create a valid OrderDetailsDTO for testing
        validOrderDTO = new OrderDetailsDTO();
        validOrderDTO.setUserId(1L);
        validOrderDTO.setName("Test User");
        validOrderDTO.setEmail("test@example.com");

        validOrderDTO.setAddress("123 Test Street");
        validOrderDTO.setDistrict("Test District");
        validOrderDTO.setPostalCode("12345");
        validOrderDTO.setItemName("Aquarium Fish");
        validOrderDTO.setQuantity(2);
        validOrderDTO.setItemPrice(100.0);
        validOrderDTO.setTotalPrice(200.0);

        // Create a mock OrderDetails instance
        mockOrder = new OrderDetails();
        mockOrder.setOrderid(1L);
        mockOrder.setUserId(1L);
        mockOrder.setName("Test User");
        mockOrder.setEmail("test@example.com");

        mockOrder.setAddress("123 Test Street");
        mockOrder.setDistrict("Test District");
        mockOrder.setPostalCode("12345");
        mockOrder.setItemName("Aquarium Fish");
        mockOrder.setQuantity(2);
        mockOrder.setItemPrice(100.0);
        mockOrder.setTotalPrice(200.0);

        // Create a mock list of OrderDetails
        mockOrdersList = Arrays.asList(mockOrder);
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderDetailsRepository.save(any(OrderDetails.class))).thenReturn(mockOrder);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        OrderDetails result = orderDetailsService.createOrder(validOrderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getOrderid());
        assertEquals("Test User", result.getName());
        assertEquals(200.0, result.getTotalPrice());

        // Verify repository was called
        verify(orderDetailsRepository, times(1)).save(orderDetailsCaptor.capture());
        OrderDetails savedOrder = orderDetailsCaptor.getValue();
        assertEquals(validOrderDTO.getName(), savedOrder.getName());
        assertEquals(validOrderDTO.getEmail(), savedOrder.getEmail());
        assertEquals(validOrderDTO.getTotalPrice(), savedOrder.getTotalPrice());

        // Verify email service was called with correct parameters
        verify(emailService, times(1)).sendEmail(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                bodyCaptor.capture()
        );
        assertEquals("test@example.com", emailCaptor.getValue());
        assertTrue(subjectCaptor.getValue().contains("Order Confirmation"));
        assertTrue(bodyCaptor.getValue().contains("Test User"));
        assertTrue(bodyCaptor.getValue().contains("Aquarium Fish"));
    }

    @Test
    void createOrder_RepositoryException() {
        // Arrange
        when(orderDetailsRepository.save(any(OrderDetails.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderDetailsService.createOrder(validOrderDTO);
        });

        assertTrue(exception.getMessage().contains("Failed to create order"));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void createOrder_EmailServiceException() {
        // Arrange
        when(orderDetailsRepository.save(any(OrderDetails.class))).thenReturn(mockOrder);
        doThrow(new RuntimeException("Email service error"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act - Should not throw exception even if email service fails
        OrderDetails result = orderDetailsService.createOrder(validOrderDTO);

        // Assert - Order should still be created
        assertNotNull(result);
        assertEquals(1L, result.getOrderid());

        // Verify repository was still called
        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    void getOrdersByUserId_Success() {
        // Arrange
        when(orderDetailsRepository.findByUserId(anyLong())).thenReturn(mockOrdersList);

        // Act
        List<OrderDetails> result = orderDetailsService.getOrdersByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderid());
        assertEquals("Test User", result.get(0).getName());

        // Verify repository was called
        verify(orderDetailsRepository, times(1)).findByUserId(1L);
    }

    @Test
    void getAllOrders_Success() {
        // Arrange
        when(orderDetailsRepository.findAll()).thenReturn(mockOrdersList);

        // Act
        List<OrderDetails> result = orderDetailsService.getAllOrders();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderid());
        assertEquals("Aquarium Fish", result.get(0).getItemName());

        // Verify repository was called
        verify(orderDetailsRepository, times(1)).findAll();
    }

    @Test
    void deleteOrder_Success() {
        // Arrange
        when(orderDetailsRepository.existsById(anyLong())).thenReturn(true);
        when(orderDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        doNothing().when(orderDetailsRepository).deleteById(anyLong());
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        orderDetailsService.deleteOrder(1L);

        // Assert - No exception is thrown

        // Verify repository methods were called
        verify(orderDetailsRepository, times(1)).existsById(1L);
        verify(orderDetailsRepository, times(1)).findById(1L);
        verify(orderDetailsRepository, times(1)).deleteById(1L);

        // Verify email service was called with correct parameters
        verify(emailService, times(1)).sendEmail(
                emailCaptor.capture(),
                subjectCaptor.capture(),
                bodyCaptor.capture()
        );
        assertEquals("test@example.com", emailCaptor.getValue());
        assertTrue(subjectCaptor.getValue().contains("Order Cancellation"));
        assertTrue(bodyCaptor.getValue().contains("Test User"));
        assertTrue(bodyCaptor.getValue().contains("cancelled successfully"));
    }

    @Test
    void deleteOrder_OrderNotFound() {
        // Arrange
        when(orderDetailsRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderDetailsService.deleteOrder(1L);
        });

        assertTrue(exception.getMessage().contains("Order not found"));

        // Verify repository was called but no deletion happened
        verify(orderDetailsRepository, times(1)).existsById(1L);
        verify(orderDetailsRepository, never()).deleteById(anyLong());
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void deleteOrder_EmailServiceException() {
        // Arrange
        when(orderDetailsRepository.existsById(anyLong())).thenReturn(true);
        when(orderDetailsRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        doNothing().when(orderDetailsRepository).deleteById(anyLong());
        doThrow(new RuntimeException("Email service error"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act - Should still delete the order even if email service fails
        orderDetailsService.deleteOrder(1L);

        // Verify repository methods were still called
        verify(orderDetailsRepository, times(1)).existsById(1L);
        verify(orderDetailsRepository, times(1)).findById(1L);
        verify(orderDetailsRepository, times(1)).deleteById(1L);
    }
}
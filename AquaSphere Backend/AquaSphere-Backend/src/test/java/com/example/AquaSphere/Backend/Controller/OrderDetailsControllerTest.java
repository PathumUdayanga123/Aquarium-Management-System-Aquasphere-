package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.OrderDetailsDTO;
import com.example.AquaSphere.Backend.Entity.OrderDetails;
import com.example.AquaSphere.Backend.Service.OrderDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderDetailsService orderDetailsService;

    @InjectMocks
    private OrderDetailsController orderDetailsController;

    private ObjectMapper objectMapper;

    private OrderDetailsDTO validOrderDTO;
    private OrderDetails mockOrder;
    private List<OrderDetails> mockOrdersList;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(orderDetailsController).build();

        // Initialize ObjectMapper
        objectMapper = new ObjectMapper();

        // Create a valid OrderDetailsDTO for testing
        validOrderDTO = new OrderDetailsDTO();
        validOrderDTO.setUserId(1L);
        validOrderDTO.setName("Test User");
        validOrderDTO.setEmail("test@example.com");
         // Added phone field
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
         // Added phone field
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
    void createOrder_Success() throws Exception {
        // Mock the service method
        when(orderDetailsService.createOrder(any(OrderDetailsDTO.class))).thenReturn(mockOrder);

        // Perform POST request and validate response
        mockMvc.perform(post("/api/order_details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderid", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.totalPrice", is(200.0)));

        // Verify that service method was called
        verify(orderDetailsService, times(1)).createOrder(any(OrderDetailsDTO.class));
    }

    @Test
    void createOrder_Failure() throws Exception {
        // Mock service method to throw exception
        when(orderDetailsService.createOrder(any(OrderDetailsDTO.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Perform POST request and validate error response
        mockMvc.perform(post("/api/order_details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validOrderDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Failed to create order")))
                .andExpect(jsonPath("$.message", is("Database error")));

        verify(orderDetailsService, times(1)).createOrder(any(OrderDetailsDTO.class));
    }

    @Test
    void getOrdersByUserId_Success() throws Exception {
        // Mock service method
        when(orderDetailsService.getOrdersByUserId(anyLong())).thenReturn(mockOrdersList);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/order_details/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderid", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test User")));

        verify(orderDetailsService, times(1)).getOrdersByUserId(1L);
    }

    @Test
    void getOrdersByUserId_Failure() throws Exception {
        // Mock service method to throw exception
        when(orderDetailsService.getOrdersByUserId(anyLong()))
                .thenThrow(new RuntimeException("User not found"));

        // Perform GET request and validate error response
        mockMvc.perform(get("/api/order_details/user/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Failed to retrieve orders")))
                .andExpect(jsonPath("$.message", is("User not found")));

        verify(orderDetailsService, times(1)).getOrdersByUserId(1L);
    }

    @Test
    void getAllOrders_Success() throws Exception {
        // Mock service method
        when(orderDetailsService.getAllOrders()).thenReturn(mockOrdersList);

        // Perform GET request and validate response
        mockMvc.perform(get("/api/order_details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].orderid", is(1)))
                .andExpect(jsonPath("$[0].itemName", is("Aquarium Fish")));

        verify(orderDetailsService, times(1)).getAllOrders();
    }

    @Test
    void getAllOrders_Failure() throws Exception {
        // Mock service method to throw exception
        when(orderDetailsService.getAllOrders())
                .thenThrow(new RuntimeException("Database error"));

        // Perform GET request and validate error response
        mockMvc.perform(get("/api/order_details"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Failed to retrieve all orders")))
                .andExpect(jsonPath("$.message", is("Database error")));

        verify(orderDetailsService, times(1)).getAllOrders();
    }

    @Test
    void deleteOrder_Success() throws Exception {
        // Mock service method (void return type)
        doNothing().when(orderDetailsService).deleteOrder(anyLong());

        // Perform DELETE request and validate response
        mockMvc.perform(delete("/api/order_details/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order deleted successfully")));

        verify(orderDetailsService, times(1)).deleteOrder(1L);
    }

    @Test
    void deleteOrder_Failure() throws Exception {
        // Mock service method to throw exception
        doThrow(new RuntimeException("Order not found"))
                .when(orderDetailsService).deleteOrder(anyLong());

        // Perform DELETE request and validate error response
        mockMvc.perform(delete("/api/order_details/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Failed to delete order")))
                .andExpect(jsonPath("$.message", is("Order not found")));

        verify(orderDetailsService, times(1)).deleteOrder(1L);
    }
}
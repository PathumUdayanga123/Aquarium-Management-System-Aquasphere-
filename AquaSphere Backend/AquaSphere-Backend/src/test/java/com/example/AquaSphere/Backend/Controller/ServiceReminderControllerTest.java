package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Service.ServiceReminderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceReminderService serviceReminderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test saveDetails - should save and return service reminder")
    void saveDetails() throws Exception {
        // Create test entity
        ServiceReminderEntity entity = new ServiceReminderEntity();

        entity.setService_type("Water Change");


        entity.setService_status("Pending");

        // Mock service response
        when(serviceReminderService.saveDetails(any(ServiceReminderEntity.class))).thenReturn(entity);

        // Perform POST request and verify
        mockMvc.perform(post("/reminder/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entity)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service_id").value(1))
                .andExpect(jsonPath("$.service_type").value("Water Change"))
                .andExpect(jsonPath("$.service_description").value("Complete water change for Tank 1"))
                .andExpect(jsonPath("$.service_date").value("2025-05-20"))
                .andExpect(jsonPath("$.service_status").value("Pending"));

        // Verify service method was called
        verify(serviceReminderService, times(1)).saveDetails(any(ServiceReminderEntity.class));
    }

    @Test
    @DisplayName("Test getAllDetails - should return list of service reminders")
    void getAllDetails() throws Exception {
        // Create test data
        List<ServiceReminderEntity> reminders = new ArrayList<>();

        ServiceReminderEntity reminder1 = new ServiceReminderEntity();

        reminder1.setService_type("Water Change");


        reminder1.setService_status("Pending");

        ServiceReminderEntity reminder2 = new ServiceReminderEntity();

        reminder2.setService_type("Filter Cleaning");


        reminder2.setService_status("Pending");

        reminders.add(reminder1);
        reminders.add(reminder2);

        // Mock service response
        when(serviceReminderService.getAllDetails()).thenReturn(reminders);

        // Perform GET request and verify
        mockMvc.perform(get("/reminder/viewallservicereminders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].service_id").value(1))
                .andExpect(jsonPath("$[0].service_type").value("Water Change"))
                .andExpect(jsonPath("$[1].service_id").value(2))
                .andExpect(jsonPath("$[1].service_type").value("Filter Cleaning"))
                .andExpect(jsonPath("$[1].service_status").value("Pending"));

        // Verify service method was called
        verify(serviceReminderService, times(1)).getAllDetails();
    }
}
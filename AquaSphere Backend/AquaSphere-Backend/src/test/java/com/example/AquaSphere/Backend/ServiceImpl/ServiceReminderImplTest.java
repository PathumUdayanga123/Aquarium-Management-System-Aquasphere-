package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Repository.ServiceReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceReminderImplTest {

    @Mock
    private ServiceReminderRepository serviceReminderRepository;

    @InjectMocks
    private ServiceReminderImpl serviceReminderImpl;

    private ServiceReminderEntity pendingReminder;
    private ServiceReminderEntity completedReminder;
    private List<ServiceReminderEntity> reminderList;

    @BeforeEach
    void setUp() {
        // Create test data
        pendingReminder = new ServiceReminderEntity();

        pendingReminder.setService_type("Water Change");

        pendingReminder.setService_status("Pending");

        completedReminder = new ServiceReminderEntity();

        completedReminder.setService_type("Filter Cleaning");

        completedReminder.setService_status("Completed");

        reminderList = new ArrayList<>();
        reminderList.add(pendingReminder);
        reminderList.add(completedReminder);
    }

    @Test
    @DisplayName("Test getAllDetails - should return only Pending reminders")
    void getAllDetails() {
        // Mock repository response
        when(serviceReminderRepository.findAll()).thenReturn(reminderList);

        // Call the method to test
        List<ServiceReminderEntity> result = serviceReminderImpl.getAllDetails();

        // Verify results
        assertEquals(1, result.size());
        assertEquals("Pending", result.get(0).getService_status());
        assertEquals("Water Change", result.get(0).getService_type());

        // Verify repository was called
        verify(serviceReminderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getAllDetails - should handle empty list")
    void getAllDetails_EmptyList() {
        // Mock repository response
        when(serviceReminderRepository.findAll()).thenReturn(Collections.emptyList());

        // Call the method to test
        List<ServiceReminderEntity> result = serviceReminderImpl.getAllDetails();

        // Verify results
        assertTrue(result.isEmpty());

        // Verify repository was called
        verify(serviceReminderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getAllDetails - should handle no pending reminders")
    void getAllDetails_NoPendingReminders() {
        // Create list with only completed reminders
        List<ServiceReminderEntity> onlyCompleted = Collections.singletonList(completedReminder);

        // Mock repository response
        when(serviceReminderRepository.findAll()).thenReturn(onlyCompleted);

        // Call the method to test
        List<ServiceReminderEntity> result = serviceReminderImpl.getAllDetails();

        // Verify results
        assertTrue(result.isEmpty());

        // Verify repository was called
        verify(serviceReminderRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test saveDetails - should save reminder with status")
    void saveDetails() {
        // Mock repository response
        when(serviceReminderRepository.save(any(ServiceReminderEntity.class))).thenReturn(pendingReminder);

        // Call the method to test
        ServiceReminderEntity result = serviceReminderImpl.saveDetails(pendingReminder);

        // Verify results
        assertNotNull(result);
        assertEquals("Pending", result.getService_status());
        assertEquals("Water Change", result.getService_type());

        // Verify repository was called
        verify(serviceReminderRepository, times(1)).save(pendingReminder);
    }

    @Test
    @DisplayName("Test saveDetails - should set default status when null")
    void saveDetails_NullStatus() {
        // Create entity with null status
        ServiceReminderEntity nullStatusReminder = new ServiceReminderEntity();

        nullStatusReminder.setService_type("Equipment Check");
        nullStatusReminder.setService_status(null);

        // Create expected result with default status
        ServiceReminderEntity expectedResult = new ServiceReminderEntity();

        expectedResult.setService_type("Equipment Check");
        expectedResult.setService_status("Pending");

        // Mock repository behavior
        when(serviceReminderRepository.save(any(ServiceReminderEntity.class))).thenReturn(expectedResult);

        // Call the method to test
        ServiceReminderEntity result = serviceReminderImpl.saveDetails(nullStatusReminder);

        // Verify results
        assertEquals("Pending", result.getService_status());

        // Verify repository was called with correct argument
        verify(serviceReminderRepository, times(1)).save(nullStatusReminder);
    }

    @Test
    @DisplayName("Test saveDetails - should set default status when empty")
    void saveDetails_EmptyStatus() {
        // Create entity with empty status
        ServiceReminderEntity emptyStatusReminder = new ServiceReminderEntity();

        emptyStatusReminder.setService_type("Tank Inspection");
        emptyStatusReminder.setService_status("");

        // Create expected result with default status
        ServiceReminderEntity expectedResult = new ServiceReminderEntity();

        expectedResult.setService_type("Tank Inspection");
        expectedResult.setService_status("Pending");

        // Mock repository behavior
        when(serviceReminderRepository.save(any(ServiceReminderEntity.class))).thenReturn(expectedResult);

        // Call the method to test
        ServiceReminderEntity result = serviceReminderImpl.saveDetails(emptyStatusReminder);

        // Verify results
        assertEquals("Pending", result.getService_status());

        // Verify repository was called with correct argument
        verify(serviceReminderRepository, times(1)).save(emptyStatusReminder);
    }
}
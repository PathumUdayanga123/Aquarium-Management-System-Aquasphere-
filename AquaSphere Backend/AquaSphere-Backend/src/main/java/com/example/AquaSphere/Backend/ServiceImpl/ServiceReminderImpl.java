package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Repository.ServiceReminderRepository;
import com.example.AquaSphere.Backend.Service.ServiceReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceReminderImpl implements ServiceReminderService {

    @Autowired
    private ServiceReminderRepository serviceReminderRepository;

    @Override
    public List<ServiceReminderEntity> getAllDetails() {
        List<ServiceReminderEntity> allRecords = serviceReminderRepository.findAll();

        if (allRecords.isEmpty()) {
            System.out.println("No service reminders found in the database.");
        } else {
            System.out.println("Fetched " + allRecords.size() + " service reminders.");
        }

        return allRecords.stream()
                .filter(service -> service.getService_status() == null || service.getService_status().trim().isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public ServiceReminderEntity saveDetails(ServiceReminderEntity serviceReminderEntity) {
        // Ensure a default status if it's null or empty
        if (serviceReminderEntity.getService_status() == null || serviceReminderEntity.getService_status().trim().isEmpty()) {
            serviceReminderEntity.setService_status("Pending");
        }

        return serviceReminderRepository.save(serviceReminderEntity);
    }
}
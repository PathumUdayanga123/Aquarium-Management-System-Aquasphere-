package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import com.example.AquaSphere.Backend.Service.ServiceReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reminder")
public class ServiceReminderController {

    @Autowired
    private ServiceReminderService serviceReminderService;

    @PostMapping("/save")
    public ServiceReminderEntity saveDetails(@RequestBody ServiceReminderEntity serviceReminderEntity) {
        return serviceReminderService.saveDetails(serviceReminderEntity);
    }

    @GetMapping("/viewallservicereminders")
    public List<ServiceReminderEntity> getAllDetails() {
        return serviceReminderService.getAllDetails();
    }
}

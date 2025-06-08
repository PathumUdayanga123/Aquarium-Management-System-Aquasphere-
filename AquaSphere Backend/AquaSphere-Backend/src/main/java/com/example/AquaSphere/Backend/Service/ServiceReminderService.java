package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import java.util.List;

public interface ServiceReminderService {
    List<ServiceReminderEntity> getAllDetails();
    ServiceReminderEntity saveDetails(ServiceReminderEntity serviceReminderEntity);
}

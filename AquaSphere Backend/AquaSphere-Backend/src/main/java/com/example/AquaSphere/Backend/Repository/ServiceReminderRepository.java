package com.example.AquaSphere.Backend.Repository;

import com.example.AquaSphere.Backend.Entity.ServiceReminderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceReminderRepository extends JpaRepository<ServiceReminderEntity, Integer> {
}
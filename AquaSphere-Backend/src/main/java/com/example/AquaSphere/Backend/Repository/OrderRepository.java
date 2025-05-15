package com.example.AquaSphere.Backend.Repository;

import com.example.AquaSphere.Backend.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(Long userId);
    List<OrderEntity> findByOrderDate(LocalDate date);
    List<OrderEntity> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);


}
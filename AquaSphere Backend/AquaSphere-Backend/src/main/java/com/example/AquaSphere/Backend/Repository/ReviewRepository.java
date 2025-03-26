package com.example.AquaSphere.Backend.Repository;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
}

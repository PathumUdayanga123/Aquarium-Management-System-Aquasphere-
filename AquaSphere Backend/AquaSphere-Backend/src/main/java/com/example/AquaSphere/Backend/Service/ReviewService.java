package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;

import java.util.List;

public interface ReviewService {

    List<ReviewEntity> getAllDetails();

    ReviewEntity saveDetails(ReviewEntity reviewEntity);

    void deleteItem(String reviewId);
}
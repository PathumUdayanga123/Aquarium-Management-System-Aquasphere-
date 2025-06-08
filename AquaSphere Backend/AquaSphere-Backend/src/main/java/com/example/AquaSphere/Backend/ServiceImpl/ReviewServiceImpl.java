package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;
import com.example.AquaSphere.Backend.Repository.ReviewRepository;
import com.example.AquaSphere.Backend.Service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<ReviewEntity> getAllDetails() {
        return reviewRepository.findAll();
    }

    @Override
    public ReviewEntity saveDetails(ReviewEntity reviewEntity) {
        return reviewRepository.save(reviewEntity);
    }

    @Override
    public void deleteItem(String review_id) {
        try {
            int id = Integer.parseInt(review_id);
            Optional<ReviewEntity> review = reviewRepository.findById(id);
            review.ifPresent(reviewRepository::delete);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid review ID format: " + review_id);
        }
    }
}
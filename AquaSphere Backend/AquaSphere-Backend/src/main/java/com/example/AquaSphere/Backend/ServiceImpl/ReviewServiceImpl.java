package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;
import com.example.AquaSphere.Backend.Repository.ReviewRepository;
import com.example.AquaSphere.Backend.Service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}

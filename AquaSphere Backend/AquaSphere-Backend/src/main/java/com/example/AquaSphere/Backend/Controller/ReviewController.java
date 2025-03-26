package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;
import com.example.AquaSphere.Backend.Service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/reviewsave")
    public ReviewEntity saveDetails(@RequestBody ReviewEntity reviewEntity) {
        return reviewService.saveDetails(reviewEntity);
    }

    @GetMapping("/viewreviews")
    public List<ReviewEntity> getAllReviews() {
        return reviewService.getAllDetails();
    }
}

package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;
import com.example.AquaSphere.Backend.Repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewEntity testReview1;
    private ReviewEntity testReview2;
    private List<ReviewEntity> reviewList;

    @BeforeEach
    void setUp() {
        // Create test review objects
        testReview1 = new ReviewEntity();
        // Set properties for testReview1 according to your entity structure
        // For example: testReview1.setId(1); testReview1.setContent("Great aquarium service!");

        testReview2 = new ReviewEntity();
        // Set properties for testReview2

        reviewList = Arrays.asList(testReview1, testReview2);
    }

    @Test
    @DisplayName("Should return all review details")
    void getAllDetails_ShouldReturnAllReviews() {
        // Given
        when(reviewRepository.findAll()).thenReturn(reviewList);

        // When
        List<ReviewEntity> result = reviewService.getAllDetails();

        // Then
        assertEquals(2, result.size());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save review details")
    void saveDetails_ShouldReturnSavedReview() {
        // Given
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(testReview1);

        // When
        ReviewEntity savedReview = reviewService.saveDetails(testReview1);

        // Then
        assertNotNull(savedReview);
        verify(reviewRepository, times(1)).save(testReview1);
    }

    @Test
    @DisplayName("Should delete review by ID when review exists")
    void deleteItem_WhenReviewExists_ShouldDeleteReview() {
        // Given
        int reviewId = 1;
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(testReview1));
        doNothing().when(reviewRepository).delete(testReview1);

        // When
        reviewService.deleteItem(String.valueOf(reviewId));

        // Then
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, times(1)).delete(testReview1);
    }

    @Test
    @DisplayName("Should do nothing when deleting non-existent review")
    void deleteItem_WhenReviewDoesNotExist_ShouldDoNothing() {
        // Given
        int reviewId = 999;
        String reviewIdStr = String.valueOf(reviewId);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When
        reviewService.deleteItem(reviewIdStr);

        // Then
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw exception when review ID is invalid")
    void deleteItem_WhenReviewIdIsInvalid_ShouldThrowException() {
        // Given
        String invalidReviewId = "abc";

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.deleteItem(invalidReviewId);
        });

        assertEquals("Invalid review ID format: " + invalidReviewId, exception.getMessage());
        verify(reviewRepository, never()).findById(any());
        verify(reviewRepository, never()).delete(any());
    }
}
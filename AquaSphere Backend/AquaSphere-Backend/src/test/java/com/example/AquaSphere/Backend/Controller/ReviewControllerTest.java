package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.Entity.ReviewEntity;
import com.example.AquaSphere.Backend.Service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("POST /Reviews/reviewsave - Success")
    void saveDetails_ShouldReturnSavedReview() throws Exception {
        // Given
        when(reviewService.saveDetails(any(ReviewEntity.class))).thenReturn(testReview1);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/Reviews/reviewsave")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testReview1)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("GET /Reviews/viewreviews - Success")
    void getAllReviews_ShouldReturnAllReviews() throws Exception {
        // Given
        when(reviewService.getAllDetails()).thenReturn(reviewList);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/Reviews/viewreviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andDo(print());
    }

    @Test
    @DisplayName("DELETE /Reviews/delete/{review_id} - Success")
    void deleteItem_ShouldReturnNoContent() throws Exception {
        // Given
        String reviewId = "1";
        doNothing().when(reviewService).deleteItem(reviewId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/Reviews/delete/{review_id}", reviewId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
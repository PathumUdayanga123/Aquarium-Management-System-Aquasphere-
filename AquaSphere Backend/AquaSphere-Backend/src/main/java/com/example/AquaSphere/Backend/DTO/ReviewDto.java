package com.example.AquaSphere.Backend.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class ReviewDto {
    private int review_id;
    private String review_description;
    private String reviewer_name;
    private LocalDate review_date = LocalDate.now();
    private LocalTime review_time = LocalTime.now();
}

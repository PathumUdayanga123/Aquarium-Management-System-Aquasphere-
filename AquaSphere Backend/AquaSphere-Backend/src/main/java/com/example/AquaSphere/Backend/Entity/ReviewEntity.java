package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
@Entity
@Table(name = "reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;

    @Column(nullable = false)
    private String review_description;

    @Column(nullable = false)
    private String reviewer_name;

    private LocalDate review_date = LocalDate.now();
    private LocalTime review_time = LocalTime.now();
}

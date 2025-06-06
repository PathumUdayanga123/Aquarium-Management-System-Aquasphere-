package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;



@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String postalCode;

    @Column(unique = true, nullable = false)
    private String nic;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String contactNo;

    @Column
    private String otp;

    @Column
    private LocalDateTime otpGeneratedTime;

    @Column(nullable = false)
    private String role = "USER"; // Default role is USER
}
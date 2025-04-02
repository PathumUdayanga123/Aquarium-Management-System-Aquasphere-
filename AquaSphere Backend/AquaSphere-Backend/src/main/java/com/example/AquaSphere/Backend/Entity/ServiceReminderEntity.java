package com.example.AquaSphere.Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "service_reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReminderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int service_id;

    @Column(name = "service_type", nullable = false)
    private String service_type;

    @Column(name = "applicant_name", nullable = false)
    private String applicant_name;

    @Column(name = "applicant_address")
    private String applicant_address;

    @Column(name = "applicant_province")
    private String applicant_province;

    @Column(name = "applicant_mobilenumber", nullable = false)
    private String applicant_mobilenumber;

    @Column(name = "applicant_email")
    private String applicant_email;

    @Column(name = "service_status")
    private String service_status;

    @Column(name = "service_date")
    private LocalDate service_date;

    @Column(name = "service_time")
    private LocalTime service_time;
}

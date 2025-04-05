package com.example.AquaSphere.Backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ServiceReminderDto {
    private int service_id;
    private String service_type;
    private String applicant_name;
    private String applicant_address;
    private String applicant_province;
    private String applicant_mobilenumber;
    private String applicant_email;
    private String service_status;
    private LocalDate service_date;
    private LocalTime service_time;
    private String applicant_district;
}
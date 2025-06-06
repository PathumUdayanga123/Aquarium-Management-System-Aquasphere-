package com.example.AquaSphere.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String name;
    private String email;
    private String address;
    private String postalCode;
    private String nic;
    private String password;
    private String contactNo;
    private String role = "USER"; // Default role

}


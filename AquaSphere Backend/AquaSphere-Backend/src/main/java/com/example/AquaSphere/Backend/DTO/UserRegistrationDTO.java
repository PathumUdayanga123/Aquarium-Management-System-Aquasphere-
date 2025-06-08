package com.example.AquaSphere.Backend.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Postal code cannot be empty")
    private String postalCode;

    @NotBlank(message = "NIC cannot be empty")
    private String nic;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Contact number cannot be empty")
    private String contactNo;

    private String role = "USER"; // Default role
}
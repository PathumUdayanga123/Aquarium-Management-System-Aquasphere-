package com.example.AquaSphere.Backend.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

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
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getNic() {
        return nic;
    }

    public String getPassword() {
        return password;
    }

    public String getContactNo() {
        return contactNo;
    }
}


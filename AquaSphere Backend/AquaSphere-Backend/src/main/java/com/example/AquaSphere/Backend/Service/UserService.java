package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;

import java.util.List;

public interface UserService {
    // Existing methods
    User registerNewUser(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException;
    boolean verifyOTP(String email, String otp);
    User findByEmail(String email);
    void resendOTP(String email);

    // New methods for update and delete
    User updateUser(Long userId, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    User getUserById(Long userId);
}
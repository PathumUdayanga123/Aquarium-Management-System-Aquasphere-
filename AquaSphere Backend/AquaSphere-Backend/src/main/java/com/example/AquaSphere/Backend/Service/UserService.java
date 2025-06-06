package com.example.AquaSphere.Backend.Service;

import com.example.AquaSphere.Backend.DTO.LoginDTO;
import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;

import java.util.List;

public interface UserService {
    User registerNewUser(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException;
    User registerNewAdmin(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException;
    boolean verifyOTP(String email, String otp);
    User findByEmail(String email);
    void resendOTP(String email);
    User updateUser(Long userId, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    User getUserById(Long userId);
    User login(LoginDTO loginDTO);
    void logout(Long userId);

}
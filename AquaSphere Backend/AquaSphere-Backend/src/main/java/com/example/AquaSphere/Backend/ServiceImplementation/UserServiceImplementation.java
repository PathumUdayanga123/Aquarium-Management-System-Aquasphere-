package com.example.AquaSphere.Backend.ServiceImplementation;

import com.example.AquaSphere.Backend.DTO.LoginDTO;
import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;
import com.example.AquaSphere.Backend.Exception.UserNotFoundException;
import com.example.AquaSphere.Backend.Repository.UserRepository;
import com.example.AquaSphere.Backend.Service.EmailService;
import com.example.AquaSphere.Backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerNewUser(UserRegistrationDTO registrationDTO) throws UserAlreadyExistsException {
        // Check if user already exists
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use: " + registrationDTO.getEmail());
        }

        // Create new user
        User newUser = new User();
        newUser.setName(registrationDTO.getName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setAddress(registrationDTO.getAddress());
        newUser.setPostalCode(registrationDTO.getPostalCode());
        newUser.setNic(registrationDTO.getNic());
        newUser.setContactNo(registrationDTO.getContactNo());

        // Encode password
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Generate OTP
        String otp = generateOTP();
        newUser.setOtp(otp);
        newUser.setOtpGeneratedTime(LocalDateTime.now());

        // Send OTP via email
        emailService.sendOTPEmail(newUser.getEmail(), otp);

        // Save user
        return userRepository.save(newUser);
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Check if OTP matches and is not expired (e.g., valid for 10 minutes)
        return user.getOtp().equals(otp) &&
                user.getOtpGeneratedTime().plusMinutes(10).isAfter(LocalDateTime.now());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional
    public void resendOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Generate new OTP
        String newOtp = generateOTP();

        // Update user's OTP and generation time
        user.setOtp(newOtp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        // Save updated user
        userRepository.save(user);

        // Send new OTP via email
        emailService.sendOTPEmail(email, newOtp);
    }

    // Helper method to generate OTP
    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        // Find the user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Update user details
        existingUser.setName(userUpdateDTO.getName());
        existingUser.setAddress(userUpdateDTO.getAddress());
        existingUser.setPostalCode(userUpdateDTO.getPostalCode());
        existingUser.setContactNo(userUpdateDTO.getContactNo());

        // Save and return the updated user
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Delete the user
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Override
    public User login(LoginDTO loginDTO) {
        // Find user by email
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        // Check if password matches
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return user;
    }
}
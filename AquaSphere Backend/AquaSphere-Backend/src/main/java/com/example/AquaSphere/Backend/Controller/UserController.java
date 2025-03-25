package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;
import com.example.AquaSphere.Backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Authentication-related endpoints
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User registeredUser = userService.registerNewUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully. Please verify OTP.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        boolean isVerified = userService.verifyOTP(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
    }

    // User Management Endpoints
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        User updatedUser = userService.updateUser(userId, userUpdateDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
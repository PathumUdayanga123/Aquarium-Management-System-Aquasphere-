package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;
import com.example.AquaSphere.Backend.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            // Force role to be ADMIN
            registrationDTO.setRole("ADMIN");
            User registeredAdmin = userService.registerNewAdmin(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Admin user registered successfully. Please verify OTP.");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
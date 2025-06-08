package com.example.AquaSphere.Backend.ServiceImpl;

import com.example.AquaSphere.Backend.DTO.LoginDTO;
import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;
import com.example.AquaSphere.Backend.Exception.UserNotFoundException;
import com.example.AquaSphere.Backend.Repository.UserRepository;
import com.example.AquaSphere.Backend.Service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    private UserRegistrationDTO registrationDTO;
    private UserUpdateDTO updateDTO;
    private LoginDTO loginDTO;
    private User user;

    @BeforeEach
    void setUp() {
        // Initialize test data
        registrationDTO = new UserRegistrationDTO();
        registrationDTO.setName("Test User");
        registrationDTO.setEmail("test@example.com");
        registrationDTO.setPassword("Test@123");
        registrationDTO.setAddress("123 Test Street");
        registrationDTO.setPostalCode("12345");
        registrationDTO.setNic("123456789X");
        registrationDTO.setContactNo("1234567890");
        registrationDTO.setRole("USER");

        updateDTO = new UserUpdateDTO();
        updateDTO.setName("Updated User");
        updateDTO.setAddress("456 Update Street");
        updateDTO.setPostalCode("54321");
        updateDTO.setContactNo("0987654321");

        loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("Test@123");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setAddress("123 Test Street");
        user.setPostalCode("12345");
        user.setNic("123456789X");
        user.setContactNo("1234567890");
        user.setRole("USER");
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now());
    }

    @Test
    void registerNewUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(emailService).sendOTPEmail(anyString(), anyString());

        // Act
        User result = userService.registerNewUser(registrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());

        // Verify interactions
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("Test@123");
        verify(userRepository).save(any(User.class));
        verify(emailService).sendOTPEmail(eq("test@example.com"), anyString());
    }

    @Test
    void registerNewUser_EmailAlreadyExists() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        UserAlreadyExistsException exception = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.registerNewUser(registrationDTO)
        );
        assertTrue(exception.getMessage().contains("Email already in use"));

        // Verify interactions
        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendOTPEmail(anyString(), anyString());
    }

    @Test
    void registerNewAdmin_Success() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        user.setRole("ADMIN");
        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(emailService).sendOTPEmail(anyString(), anyString());

        // Act
        User result = userService.registerNewAdmin(registrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getRole());

        // Verify that role is set to ADMIN in the DTO
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("ADMIN", userCaptor.getValue().getRole());
    }

    @Test
    void verifyOTP_ValidOTP() {
        // Arrange
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(5)); // 5 minutes ago (still valid)
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.verifyOTP("test@example.com", "123456");

        // Assert
        assertTrue(result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void verifyOTP_InvalidOTP() {
        // Arrange
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(5));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.verifyOTP("test@example.com", "654321"); // Wrong OTP

        // Assert
        assertFalse(result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void verifyOTP_ExpiredOTP() {
        // Arrange
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusMinutes(15)); // 15 minutes ago (expired)
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.verifyOTP("test@example.com", "123456");

        // Assert
        assertFalse(result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_ExistingUser() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByEmail("test@example.com");

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void findByEmail_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.findByEmail("nonexistent@example.com")
        );
        assertTrue(exception.getMessage().contains("User not found with email"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void resendOTP_Success() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        doNothing().when(emailService).sendOTPEmail(anyString(), anyString());

        // Act
        userService.resendOTP("test@example.com");

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertNotNull(userCaptor.getValue().getOtp());
        assertNotNull(userCaptor.getValue().getOtpGeneratedTime());
        verify(emailService).sendOTPEmail(eq("test@example.com"), anyString());
    }

    @Test
    void resendOTP_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.resendOTP("nonexistent@example.com")
        );
        assertTrue(exception.getMessage().contains("User not found with email"));
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendOTPEmail(anyString(), anyString());
    }

    @Test
    void updateUser_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("test@example.com");
        updatedUser.setAddress("456 Update Street");
        updatedUser.setPostalCode("54321");
        updatedUser.setContactNo("0987654321");
        updatedUser.setRole("USER");

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        User result = userService.updateUser(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("456 Update Street", result.getAddress());
        assertEquals("54321", result.getPostalCode());
        assertEquals("0987654321", result.getContactNo());

        // Verify interactions
        verify(userRepository).findById(1L);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("Updated User", userCaptor.getValue().getName());
        assertEquals("456 Update Street", userCaptor.getValue().getAddress());
        assertEquals("54321", userCaptor.getValue().getPostalCode());
        assertEquals("0987654321", userCaptor.getValue().getContactNo());
    }

    @Test
    void updateUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(99L, updateDTO)
        );
        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(99L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(any(User.class));

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.deleteUser(99L)
        );
        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(99L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(user);

        User secondUser = new User();
        secondUser.setId(2L);
        secondUser.setName("Second User");
        secondUser.setEmail("second@example.com");
        userList.add(secondUser);

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getName());
        assertEquals("Second User", result.get(1).getName());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserById(99L)
        );
        assertTrue(exception.getMessage().contains("User not found with id"));
        verify(userRepository).findById(99L);
    }

    @Test
    void login_Success() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Act
        User result = userService.login(loginDTO);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("Test@123", "encodedPassword");
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> userService.login(loginDTO)
        );
        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("Test@123", "encodedPassword");
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> userService.login(loginDTO)
        );
        assertEquals("Invalid email or password", exception.getMessage());
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void logout_Success() {
        // This test verifies the logout method works as expected
        // Since the implementation just logs a message, we're simply ensuring it doesn't throw exceptions

        // Act
        userService.logout(1L);

        // No assertions needed as method has no return value and only logs a message
        // The test passes if no exception is thrown
    }
}
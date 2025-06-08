package com.example.AquaSphere.Backend.Controller;

import com.example.AquaSphere.Backend.DTO.LoginDTO;
import com.example.AquaSphere.Backend.DTO.UserRegistrationDTO;
import com.example.AquaSphere.Backend.DTO.UserUpdateDTO;
import com.example.AquaSphere.Backend.Entity.User;
import com.example.AquaSphere.Backend.Exception.UserAlreadyExistsException;
import com.example.AquaSphere.Backend.Exception.UserNotFoundException;
import com.example.AquaSphere.Backend.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegistrationDTO registrationDTO;
    private UserUpdateDTO updateDTO;
    private LoginDTO loginDTO;
    private User user;
    private MockHttpSession session;

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

        session = new MockHttpSession();
    }

    @Test
    void registerUser_Success() throws Exception {
        when(userService.registerNewUser(any(UserRegistrationDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("User registered successfully")));

        verify(userService, times(1)).registerNewUser(any(UserRegistrationDTO.class));
    }

    @Test
    void registerUser_AlreadyExists() throws Exception {
        when(userService.registerNewUser(any(UserRegistrationDTO.class)))
                .thenThrow(new UserAlreadyExistsException("Email already in use"));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already in use")));

        verify(userService, times(1)).registerNewUser(any(UserRegistrationDTO.class));
    }

    @Test
    void verifyOTP_Success() throws Exception {
        when(userService.verifyOTP(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(post("/api/users/verify-otp")
                        .param("email", "test@example.com")
                        .param("otp", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OTP verified successfully")));

        verify(userService, times(1)).verifyOTP("test@example.com", "123456");
    }

    @Test
    void verifyOTP_Invalid() throws Exception {
        when(userService.verifyOTP(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/users/verify-otp")
                        .param("email", "test@example.com")
                        .param("otp", "123456"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid or expired OTP")));

        verify(userService, times(1)).verifyOTP("test@example.com", "123456");
    }

    @Test
    void updateUser_Success() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("test@example.com");
        updatedUser.setAddress("456 Update Street");
        updatedUser.setPostalCode("54321");
        updatedUser.setContactNo("0987654321");
        updatedUser.setRole("USER");

        when(userService.updateUser(eq(1L), any(UserUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated User")))
                .andExpect(jsonPath("$.address", is("456 Update Street")))
                .andExpect(jsonPath("$.postalCode", is("54321")))
                .andExpect(jsonPath("$.contactNo", is("0987654321")));

        verify(userService, times(1)).updateUser(eq(1L), any(UserUpdateDTO.class));
    }

    @Test
    void updateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(99L), any(UserUpdateDTO.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));

        verify(userService, times(1)).updateUser(eq(99L), any(UserUpdateDTO.class));
    }

    @Test
    void deleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/Delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User deleted successfully")));

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void deleteUser_NotFound() throws Exception {
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(99L);

        mockMvc.perform(delete("/api/users/Delete/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));

        verify(userService, times(1)).deleteUser(99L);
    }

    @Test
    void getAllUsers_Success() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(user);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Second User");
        user2.setEmail("second@example.com");
        user2.setRole("USER");
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Test User")))
                .andExpect(jsonPath("$[1].name", is("Second User")));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.role", is("USER")));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void getUserById_NotFound() throws Exception {
        when(userService.getUserById(99L)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("User not found")));

        verify(userService, times(1)).getUserById(99L);
    }

    @Test
    void login_Success() throws Exception {
        when(userService.login(any(LoginDTO.class))).thenReturn(user);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test User")))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.role", is("USER")))
                .andExpect(jsonPath("$.message", is("Login successful")));

        verify(userService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void login_InvalidCredentials() throws Exception {
        when(userService.login(any(LoginDTO.class))).thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid email or password")));

        verify(userService, times(1)).login(any(LoginDTO.class));
    }

    @Test
    void logout_Success() throws Exception {
        session.setAttribute("userId", 1L);
        doNothing().when(userService).logout(1L);

        mockMvc.perform(post("/api/users/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Logged out successfully")));

        verify(userService, times(1)).logout(1L);
    }

    @Test
    void logout_NoUserLoggedIn() throws Exception {
        // Session without userId attribute
        MockHttpSession emptySession = new MockHttpSession();

        mockMvc.perform(post("/api/users/logout")
                        .session(emptySession))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("No user is currently logged in")));

        verify(userService, never()).logout(anyLong());
    }
}
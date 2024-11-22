import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.TaskUserService.SecurityConfig.JwtProvider;
import com.example.TaskUserService.controller.UserController;
import com.example.TaskUserService.repository.UserRepository;
import com.example.TaskUserService.response.AuthResponse;
import com.example.TaskUserService.service.UserServiceImplementation;
import com.example.TaskUserService.usermodel.User;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserServiceImplementation customUserDetails;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserHandler_NewUser_Success() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");
        user.setMobile("1234567890");

        // Simulate no existing user with this email
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        ResponseEntity<AuthResponse> response = userController.createUserHandler(user);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        AuthResponse authResponse = response.getBody();
        assertNotNull(authResponse);
        assertTrue(authResponse.getStatus());
        assertEquals("Register Success", authResponse.getMessage());
        assertNotNull(authResponse.getJwt());
    }


    @Test
    public void testSignin_InvalidPassword_ThrowsException() {
        // Arrange
        User loginRequest = new User();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getPassword()).thenReturn("encodedPassword");

        when(customUserDetails.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class, 
            () -> userController.signin(loginRequest)
        );
    }
}
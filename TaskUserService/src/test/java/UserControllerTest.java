import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

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

    @Test
public void testGetAll_UsersExist_ReturnsOk() {
    // Arrange
    List<User> users = List.of(new User("1", "John Doe", "john@example.com", "pass", "ROLE_CUSTOMER", "Address", "1234567890"));
    when(userRepository.findAll()).thenReturn(users);

    // Act
    ResponseEntity<List<User>> response = userController.getAll();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
}

@Test
public void testGetAll_NoUsers_ReturnsNoContent() {
    // Arrange
    when(userRepository.findAll()).thenReturn(new ArrayList<>());

    // Act
    ResponseEntity<List<User>> response = userController.getAll();

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
}

@Test
public void testGetAllCustomersOnly_CustomersExist_ReturnsOk() {
    // Arrange
    User user1 = new User("1", "John Doe", "john@example.com", "pass", "ROLE_CUSTOMER", "Address", "1234567890");
    User user2 = new User("2", "Jane Doe", "jane@example.com", "pass", "USER", "Address", "9876543210");
    when(userRepository.findAll()).thenReturn(List.of(user1, user2));

    // Act
    ResponseEntity<List<User>> response = userController.getAllcustomersonly();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    assertEquals("ROLE_CUSTOMER", response.getBody().get(0).getRole());
}

@Test
public void testGetAllCustomersOnly_NoCustomers_ReturnsNoContent() {
    // Arrange
    User user = new User("1", "John Doe", "john@example.com", "pass", "USER", "Address", "1234567890");
    when(userRepository.findAll()).thenReturn(List.of(user));

    // Act
    ResponseEntity<List<User>> response = userController.getAllcustomersonly();

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
}

@Test
public void testUpdateUser_Success() {
    // Arrange
    User user = new User("1", "John Doe", "john@example.com", "pass", "ROLE_CUSTOMER", "Address", "1234567890");
    when(userRepository.save(any(User.class))).thenReturn(user);

    // Act
    ResponseEntity<User> response = userController.updateUser(user);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(user.getId(), response.getBody().getId());
}

@Test
public void testGetUserById_UserExists_ReturnsOk() {
    // Arrange
    User user = new User("1", "John Doe", "john@example.com", "pass", "ROLE_CUSTOMER", "Address", "1234567890");
    when(userRepository.findById("1")).thenReturn(Optional.of(user));

    // Act
    ResponseEntity<User> response = userController.getUserById("1");

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(user.getId(), response.getBody().getId());
}

@Test
public void testDeleteUser_UserExists_ReturnsNoContent() {
    // Arrange
    doNothing().when(userRepository).deleteById("1");

    // Act
    ResponseEntity<HttpStatus> response = userController.deleteUser("1");

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
}

@Test
public void testUpdatePasswordByEmail_UserExists_PasswordUpdated() {
    // Arrange
    String email = "john@example.com";
    String newPassword = "newPassword";
    User user = new User("1", "John Doe", email, "oldPassword", "ROLE_CUSTOMER", "Address", "1234567890");
    when(userRepository.findByEmail(email)).thenReturn(user);
    when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

    // Act
    ResponseEntity<String> response = userController.updatePasswordByEmail(email, Map.of("password", newPassword));

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Password updated successfully", response.getBody());
    verify(userRepository).save(user);
}

@Test
public void testUpdatePasswordByEmail_UserNotFound_ReturnsNotFound() {
    // Arrange
    String email = "notfound@example.com";
    when(userRepository.findByEmail(email)).thenReturn(null);

    // Act
    ResponseEntity<String> response = userController.updatePasswordByEmail(email, Map.of("password", "newPassword"));

    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("User not found", response.getBody());
}

@Test
public void testUpdatePasswordByEmail_InvalidPassword_ReturnsBadRequest() {
    // Arrange
    String email = "john@example.com";

    // Act
    ResponseEntity<String> response = userController.updatePasswordByEmail(email, Map.of("password", ""));

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Password cannot be empty", response.getBody());
}

}
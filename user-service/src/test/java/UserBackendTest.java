
import com.style_haven.user_service.domain.User;
import com.style_haven.user_service.model.UserDTO;
import com.style_haven.user_service.repos.UserRepository;
import com.style_haven.user_service.util.NotFoundException;
import com.style_haven.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserBackendTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // Setup test data before each test method
        testUser = new User();
        testUser.setUserid(1);
        testUser.setFname("John");
        testUser.setLname("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setUsername("johndoe");

        testUserDTO = new UserDTO();
        testUserDTO.setFname("John");
        testUserDTO.setLname("Doe");
        testUserDTO.setEmail("john.doe@example.com");
        testUserDTO.setUsername("johndoe");
    }

    @Test
    void testFindAll() {
        // Arrange
        List<User> userList = Arrays.asList(testUser);
        when(userRepository.findAll(any(Sort.class))).thenReturn(userList);

        // Act
        List<UserDTO> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser.getFname(), result.get(0).getFname());
        verify(userRepository).findAll(any(Sort.class));
    }

    @Test
    void testGetExistingUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        UserDTO result = userService.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getFname(), result.getFname());
        verify(userRepository).findById(1);
    }

    @Test
    void testGetNonExistingUser() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.get(999));
    }

    @Test
    void testCreateUser() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        Integer userId = userService.create(testUserDTO);

        // Assert
        assertNotNull(userId);
        assertEquals(testUser.getUserid(), userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.update(1, testUserDTO);

        // Assert
        verify(userRepository).findById(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateNonExistingUser() {
        // Arrange
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> userService.update(999, testUserDTO));
    }

    @Test
    void testDeleteUser() {
        // Act
        userService.delete(1);

        // Assert
        verify(userRepository).deleteById(1);
    }
}
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.style_haven.user_service.domain.User;
import com.style_haven.user_service.model.UserDTO;
import com.style_haven.user_service.repos.UserRepository;
import com.style_haven.user_service.service.UserService;

@SpringBootTest
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize User entity
        user = new User();
        user.setUserid(1);
        user.setFname("John");
        user.setLname("Doe");
        user.setState("NY");
        user.setCity("New York");
        user.setStreet("123 Main St");
        user.setPostalCode("10001");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setDateCreated(OffsetDateTime.now());
        user.setLastUpdated(OffsetDateTime.now());

        // Initialize UserDTO
        userDTO = new UserDTO();
        userDTO.setUserid(1);
        userDTO.setFname("John");
        userDTO.setLname("Doe");
        userDTO.setState("NY");
        userDTO.setCity("New York");
        userDTO.setStreet("123 Main St");
        userDTO.setPostalCode("10001");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setUsername("johndoe");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        Integer userId = userService.create(userDTO);
        assertNotNull(userId);
        assertEquals(userDTO.getUserid(), userId);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        UserDTO foundUser = userService.get(1);
        assertNotNull(foundUser);
        assertEquals(userDTO.getUsername(), foundUser.getUsername());

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userDTO.setCity("Los Angeles");
        userService.update(1, userDTO);

        assertEquals("Los Angeles", user.getCity());

        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        userService.delete(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}

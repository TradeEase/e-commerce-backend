package com.style_haven.user_service.service;

import com.style_haven.user_service.model.UserDTO;
import com.style_haven.user_service.repos.UserRepository;
import com.style_haven.user_service.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()  // List of authorities
        );
    }

    public boolean authenticate(String email, String rawPassword) {
        UserDTO user = userRepository.findByEmail(email);

        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return true;
    }

    public UserDTO findUserProfileByJwt(String jwt) {
        String userId = parseJwtAndGetUserId(jwt); // Implement or call a utility method for JWT parsing

        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    // JWT parsing method (implement using a JWT library like jjwt)
    private String parseJwtAndGetUserId(String jwt) {
        // Logic to parse JWT and extract user ID or email
        return "extractedUserId";
    }
}

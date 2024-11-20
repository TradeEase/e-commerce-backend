package com.example.TaskUserService.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.TaskUserService.repository.UserRepository;
import com.example.TaskUserService.usermodel.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.ArrayList;
import java.util.List;


@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    
    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository=userRepository;
    }
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with this email: " + username);
        }

        System.out.println("Loaded user: " + user.getEmail());

        // Here you return the user with authorities, and don't modify password comparison logic
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()  // no authorities
        );
    }

    public boolean authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Compare the raw password with the encoded password
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return true; // Authentication successful
    }
}

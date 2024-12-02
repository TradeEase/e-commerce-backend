package com.example.TaskUserService.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.TaskUserService.SecurityConfig.JwtProvider;
import com.example.TaskUserService.repository.UserRepository;
import com.example.TaskUserService.response.AuthResponse;
import com.example.TaskUserService.service.UserService;
import com.example.TaskUserService.service.UserServiceImplementation;
import com.example.TaskUserService.usermodel.User;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   
    @Autowired
    private UserServiceImplementation customUserDetails;
    
    // @Autowired
    // private UserService userService;




    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user)  {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String address = user.getAddress();
        String mobile = user.getMobile();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);
        if (isEmailExist != null) {
            //throw new Exception("Email Is Already Used With Another Account");

        }
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setAddress(address);
        createdUser.setMobile(mobile);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));
        
        User savedUser = userRepository.save(createdUser);
          userRepository.save(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Register Success");
        authResponse.setStatus(true);
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

    }





    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody User loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username+"-------"+password);

        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login success");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        System.out.println("Message(\"" + authResponse.getMessage() + "\");");
        System.out.println("Jwt(\"" + authResponse.getJwt() + "\");");

        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }



    
    private Authentication authenticate(String username, String password) {

        System.out.println(username+"---++----"+password);

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("Sign in in user details"+ userDetails);

        if(userDetails == null) {
            System.out.println("Sign in details - null" + userDetails);

            throw new BadCredentialsException("Invalid username and password");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())) {
            System.out.println("Sign in userDetails - password mismatch"+userDetails);

            throw new BadCredentialsException("Invalid password");

        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }

    
    // Get all users and customers
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAll() {
        try{
            List<User> UserList = new ArrayList<>();
            userRepository.findAll().forEach(UserList::add);
            if(UserList.isEmpty()) {
                return new ResponseEntity<>(UserList, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(UserList, HttpStatus.OK);
        }
        catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all customers only
    @GetMapping("/getAllcustomersonly")
    public ResponseEntity<List<User>> getAllcustomersonly() {
        try{
            List<User> customersonlyList = new ArrayList<>();
            userRepository.findAll().forEach(user -> {
                if ("ROLE_CUSTOMER".equals(user.getRole())) {
                    customersonlyList.add(user);
                }
            });
            if(customersonlyList.isEmpty()) {
                return new ResponseEntity<>(customersonlyList, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(customersonlyList, HttpStatus.OK);
        }
        catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all users only
    @GetMapping("/getAllusersonly")
    public ResponseEntity<List<User>> getAllusersonly() {
        try{
            List<User> usersonlyList = new ArrayList<>();
            userRepository.findAll().forEach(user -> {
                if ("USER".equals(user.getRole())) {
                    usersonlyList.add(user);
                }
            });
            if(usersonlyList.isEmpty()) {
                return new ResponseEntity<>(usersonlyList, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usersonlyList, HttpStatus.OK);
        }
        catch(Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Update user details
    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        try {
            User _user = userRepository.save(new User(user.getId(), user.getFullName(), user.getEmail(), user.getPassword(), user.getRole(),user.getAddress(), user.getMobile()));
            return new ResponseEntity<>(_user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Get user by id
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        User user = userRepository.findById(id).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable String id) {
    try {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }


}


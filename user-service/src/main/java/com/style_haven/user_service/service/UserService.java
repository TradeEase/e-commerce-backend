package com.style_haven.user_service.service;

import java.util.List;

import com.style_haven.user_service.model.UserDTO;



public interface UserService {

     
     public List<UserDTO> getAllUser()  ;
     
     public UserDTO findUserProfileByJwt(String jwt);
     
     public UserDTO findUserByEmail(String email) ;
     
     public UserDTO findUserById(String userId) ;

     public List<UserDTO> findAllUsers();
      
         
}

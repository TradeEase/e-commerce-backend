package com.example.TaskUserService.service;




import java.util.List;

import com.example.TaskUserService.usermodel.User;


public interface UserService {

     
     public List<User> getAllUser()  ;
     
     public User findUserProfileByJwt(String jwt);
     
     public User findUserByEmail(String email) ;
     
     public User findUserById(String userId) ;

     public List<User> findAllUsers();
      
         
}

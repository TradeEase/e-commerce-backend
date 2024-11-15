package com.style_haven.user_service.repos;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.style_haven.user_service.model.UserDTO;


@Repository
public interface UserRepository extends MongoRepository<UserDTO,String> {
    @Query("{email :?0}")
    UserDTO findByEmail(String email);
    

}

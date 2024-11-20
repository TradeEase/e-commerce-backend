package com.style_haven.user_service.repos;

import com.style_haven.user_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
}

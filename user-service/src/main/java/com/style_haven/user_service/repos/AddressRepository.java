package com.style_haven.user_service.repos;

import com.style_haven.user_service.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Integer> {
}

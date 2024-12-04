package com.style_haven.orderservice.repos;

import com.style_haven.orderservice.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Integer> {
}

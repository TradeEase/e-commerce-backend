package com.style_haven.order_service.repos;

import com.style_haven.order_service.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Integer> {
}

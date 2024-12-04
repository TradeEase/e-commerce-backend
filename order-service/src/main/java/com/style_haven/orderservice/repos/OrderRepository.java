package com.style_haven.orderservice.repos;

import com.style_haven.orderservice.domain.Cart;
import com.style_haven.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findFirstByCart(Cart cart);

}

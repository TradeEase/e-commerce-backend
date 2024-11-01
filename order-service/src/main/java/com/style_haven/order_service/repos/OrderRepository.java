package com.style_haven.order_service.repos;

import com.style_haven.order_service.domain.Cart;
import com.style_haven.order_service.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order findFirstByCart(Cart cart);

}

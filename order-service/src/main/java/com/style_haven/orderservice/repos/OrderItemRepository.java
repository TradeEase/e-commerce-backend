package com.style_haven.orderservice.repos;

import com.style_haven.orderservice.domain.Order;
import com.style_haven.orderservice.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    OrderItem findFirstByOrder(Order order);

}

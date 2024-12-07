package com.style_haven.orderservice.repos;

import com.style_haven.orderservice.domain.Order;
import com.style_haven.orderservice.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    OrderItem findFirstByOrder(Order order);
    List<OrderItem> findByOrder(Order order);

    Iterable<Object> findAllByOrder(Order order);
}
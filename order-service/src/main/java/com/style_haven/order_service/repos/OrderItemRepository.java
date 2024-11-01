package com.style_haven.order_service.repos;

import com.style_haven.order_service.domain.Order;
import com.style_haven.order_service.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    OrderItem findFirstByOrder(Order order);

}

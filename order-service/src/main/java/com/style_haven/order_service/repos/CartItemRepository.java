package com.style_haven.order_service.repos;

import com.style_haven.order_service.domain.Cart;
import com.style_haven.order_service.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findFirstByCart(Cart cart);
    List<CartItem> findByCart_CartId(Integer cartId);

}

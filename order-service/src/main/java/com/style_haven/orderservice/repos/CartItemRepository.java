package com.style_haven.orderservice.repos;

import com.style_haven.orderservice.domain.Cart;
import com.style_haven.orderservice.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    CartItem findFirstByCart(Cart cart);

}

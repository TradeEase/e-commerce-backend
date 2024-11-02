package com.style_haven.order_service.service;

import com.style_haven.order_service.domain.Cart;
import com.style_haven.order_service.domain.CartItem;
import com.style_haven.order_service.domain.Order;
import com.style_haven.order_service.model.CartDTO;
import com.style_haven.order_service.repos.CartItemRepository;
import com.style_haven.order_service.repos.CartRepository;
import com.style_haven.order_service.repos.OrderRepository;
import com.style_haven.order_service.util.NotFoundException;
import com.style_haven.order_service.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public CartService(final CartRepository cartRepository,
            final CartItemRepository cartItemRepository, final OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<CartDTO> findAll() {
        final List<Cart> carts = cartRepository.findAll(Sort.by("cartId"));
        return carts.stream()
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .toList();
    }

    public CartDTO get(final Integer cartId) {
        return cartRepository.findById(cartId)
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CartDTO cartDTO) {
        final Cart cart = new Cart();
        mapToEntity(cartDTO, cart);
        return cartRepository.save(cart).getCartId();
    }

    public void update(final Integer cartId, final CartDTO cartDTO) {
        final Cart cart = cartRepository.findById(cartId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cartDTO, cart);
        cartRepository.save(cart);
    }

    public void delete(final Integer cartId) {
        cartRepository.deleteById(cartId);
    }

    private CartDTO mapToDTO(final Cart cart, final CartDTO cartDTO) {
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setUserId(cart.getUserId());
        return cartDTO;
    }

    private Cart mapToEntity(final CartDTO cartDTO, final Cart cart) {
        cart.setUserId(cartDTO.getUserId());
        return cart;
    }

    public ReferencedWarning getReferencedWarning(final Integer cartId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Cart cart = cartRepository.findById(cartId)
                .orElseThrow(NotFoundException::new);
        final CartItem cartCartItem = cartItemRepository.findFirstByCart(cart);
        if (cartCartItem != null) {
            referencedWarning.setKey("cart.cartItem.cart.referenced");
            referencedWarning.addParam(cartCartItem.getCartItemId());
            return referencedWarning;
        }
        final Order cartOrder = orderRepository.findFirstByCart(cart);
        if (cartOrder != null) {
            referencedWarning.setKey("cart.order.cart.referenced");
            referencedWarning.addParam(cartOrder.getOrderId());
            return referencedWarning;
        }
        return null;
    }

}

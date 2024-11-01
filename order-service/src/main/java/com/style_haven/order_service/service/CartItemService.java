package com.style_haven.order_service.service;

import com.style_haven.order_service.domain.Cart;
import com.style_haven.order_service.domain.CartItem;
import com.style_haven.order_service.model.CartItemDTO;
import com.style_haven.order_service.repos.CartItemRepository;
import com.style_haven.order_service.repos.CartRepository;
import com.style_haven.order_service.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemService(final CartItemRepository cartItemRepository,
            final CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public List<CartItemDTO> findAll() {
        final List<CartItem> cartItems = cartItemRepository.findAll(Sort.by("cartItemId"));
        return cartItems.stream()
                .map(cartItem -> mapToDTO(cartItem, new CartItemDTO()))
                .toList();
    }

    public CartItemDTO get(final Integer cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .map(cartItem -> mapToDTO(cartItem, new CartItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CartItemDTO cartItemDTO) {
        final CartItem cartItem = new CartItem();
        mapToEntity(cartItemDTO, cartItem);
        return cartItemRepository.save(cartItem).getCartItemId();
    }

    public void update(final Integer cartItemId, final CartItemDTO cartItemDTO) {
        final CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cartItemDTO, cartItem);
        cartItemRepository.save(cartItem);
    }

    public void delete(final Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    private CartItemDTO mapToDTO(final CartItem cartItem, final CartItemDTO cartItemDTO) {
        cartItemDTO.setCartItemId(cartItem.getCartItemId());
        cartItemDTO.setCartId(cartItem.getCartId());
        cartItemDTO.setProductId(cartItem.getProductId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        cartItemDTO.setCart(cartItem.getCart() == null ? null : cartItem.getCart().getCartId());
        return cartItemDTO;
    }

    private CartItem mapToEntity(final CartItemDTO cartItemDTO, final CartItem cartItem) {
        cartItem.setCartId(cartItemDTO.getCartId());
        cartItem.setProductId(cartItemDTO.getProductId());
        cartItem.setQuantity(cartItemDTO.getQuantity());
        final Cart cart = cartItemDTO.getCart() == null ? null : cartRepository.findById(cartItemDTO.getCart())
                .orElseThrow(() -> new NotFoundException("cart not found"));
        cartItem.setCart(cart);
        return cartItem;
    }

}

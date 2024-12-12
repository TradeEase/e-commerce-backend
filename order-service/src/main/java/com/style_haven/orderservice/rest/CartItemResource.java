package com.style_haven.orderservice.rest;

import com.style_haven.orderservice.model.CartItemDTO;
import com.style_haven.orderservice.service.CartItemService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "api/order/cartItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartItemResource {

    private final CartItemService cartItemService;

    public CartItemResource(final CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getAllCartItems() {
        return ResponseEntity.ok(cartItemService.findAll());
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemDTO> getCartItem(
            @PathVariable(name = "cartItemId") final Integer cartItemId) {
        return ResponseEntity.ok(cartItemService.get(cartItemId));
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItemDTO>> getCartItemsByCart(
            @PathVariable(name = "cartId") final Integer cartId) {
        return ResponseEntity.ok(cartItemService.findByCart(cartId));
    }


    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createCartItem(
            @RequestBody @Valid final CartItemDTO cartItemDTO) {
        final Integer createdCartItemId = cartItemService.create(cartItemDTO);
        return new ResponseEntity<>(createdCartItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<Integer> updateCartItem(
            @PathVariable(name = "cartItemId") final Integer cartItemId,
            @RequestBody @Valid final CartItemDTO cartItemDTO) {
        cartItemService.update(cartItemId, cartItemDTO);
        return ResponseEntity.ok(cartItemId);
    }

    @DeleteMapping("/{cartItemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCartItem(
            @PathVariable(name = "cartItemId") final Integer cartItemId) {
        cartItemService.delete(cartItemId);
        return ResponseEntity.noContent().build();
    }

}

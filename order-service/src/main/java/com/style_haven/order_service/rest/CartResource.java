package com.style_haven.order_service.rest;

import com.style_haven.order_service.model.CartDTO;
import com.style_haven.order_service.service.CartService;
import com.style_haven.order_service.util.ReferencedException;
import com.style_haven.order_service.util.ReferencedWarning;
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
@RequestMapping(value = "/api/order/carts", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartResource {

    private final CartService cartService;

    public CartResource(final CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return ResponseEntity.ok(cartService.findAll());
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable(name = "cartId") final Integer cartId) {
        return ResponseEntity.ok(cartService.get(cartId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createCart(@RequestBody @Valid final CartDTO cartDTO) {
        final Integer createdCartId = cartService.create(cartDTO);
        return new ResponseEntity<>(createdCartId, HttpStatus.CREATED);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Integer> updateCart(@PathVariable(name = "cartId") final Integer cartId,
            @RequestBody @Valid final CartDTO cartDTO) {
        cartService.update(cartId, cartDTO);
        return ResponseEntity.ok(cartId);
    }

    @DeleteMapping("/{cartId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCart(@PathVariable(name = "cartId") final Integer cartId) {
        final ReferencedWarning referencedWarning = cartService.getReferencedWarning(cartId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        cartService.delete(cartId);
        return ResponseEntity.noContent().build();
    }

}

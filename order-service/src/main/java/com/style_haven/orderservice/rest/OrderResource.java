package com.style_haven.orderservice.rest;

import com.style_haven.orderservice.model.OrderDTO;
import com.style_haven.orderservice.service.OrderService;
import com.style_haven.orderservice.util.ReferencedException;
import com.style_haven.orderservice.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(final OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }
    @GetMapping("/user")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(orderService.findByUserId(userId));
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(
            @PathVariable(name = "orderId") final Integer orderId) {
        return ResponseEntity.ok(orderService.get(orderId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        final Integer createdOrderId = orderService.create(orderDTO);
        return new ResponseEntity<>(createdOrderId, HttpStatus.CREATED);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<Integer> updateOrder(
            @PathVariable(name = "orderId") final Integer orderId,
            @RequestBody @Valid final OrderDTO orderDTO) {
        orderService.update(orderId, orderDTO);
        return ResponseEntity.ok(orderId);
    }

    @DeleteMapping("/{orderId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "orderId") final Integer orderId) {
        final ReferencedWarning referencedWarning = orderService.getReferencedWarning(orderId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }

}

package com.style_haven.orderservice.rest;

import com.style_haven.orderservice.model.OrderItemDTO;
import com.style_haven.orderservice.service.OrderItemService;
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
@RequestMapping(value = "api/order/orderItems", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderItemResource {

    private final OrderItemService orderItemService;

    public OrderItemResource(final OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public ResponseEntity<List<OrderItemDTO>> getAllOrderItems() {
        return ResponseEntity.ok(orderItemService.findAll());
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getOrderItemsByOrder(
            @PathVariable(name = "orderId") final Integer orderId) {
        return ResponseEntity.ok(orderItemService.findByOrder(orderId));
    }
    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDTO> getOrderItem(
            @PathVariable(name = "orderItemId") final Integer orderItemId) {
        return ResponseEntity.ok(orderItemService.get(orderItemId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createOrderItem(
            @RequestBody @Valid final OrderItemDTO orderItemDTO) {
        final Integer createdOrderItemId = orderItemService.create(orderItemDTO);
        return new ResponseEntity<>(createdOrderItemId, HttpStatus.CREATED);
    }

    @PutMapping("/{orderItemId}")
    public ResponseEntity<Integer> updateOrderItem(
            @PathVariable(name = "orderItemId") final Integer orderItemId,
            @RequestBody @Valid final OrderItemDTO orderItemDTO) {
        orderItemService.update(orderItemId, orderItemDTO);
        return ResponseEntity.ok(orderItemId);
    }

    @DeleteMapping("/{orderItemId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable(name = "orderItemId") final Integer orderItemId) {
        orderItemService.delete(orderItemId);
        return ResponseEntity.noContent().build();
    }

}

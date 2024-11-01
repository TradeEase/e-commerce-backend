package com.style_haven.order_service.rest;

import com.style_haven.order_service.model.OrderDTO;
import com.style_haven.order_service.service.OrderService;
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

    @GetMapping("/{order}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "order") final Integer order) {
        return ResponseEntity.ok(orderService.get(order));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        final Integer createdOrder = orderService.create(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @PutMapping("/{order}")
    public ResponseEntity<Integer> updateOrder(@PathVariable(name = "order") final Integer order,
            @RequestBody @Valid final OrderDTO orderDTO) {
        orderService.update(order, orderDTO);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{order}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "order") final Integer order) {
        final ReferencedWarning referencedWarning = orderService.getReferencedWarning(order);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        orderService.delete(order);
        return ResponseEntity.noContent().build();
    }

}

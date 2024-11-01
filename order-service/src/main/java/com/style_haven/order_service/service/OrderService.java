package com.style_haven.order_service.service;

import com.style_haven.order_service.domain.Cart;
import com.style_haven.order_service.domain.Order;
import com.style_haven.order_service.domain.OrderItem;
import com.style_haven.order_service.model.OrderDTO;
import com.style_haven.order_service.repos.CartRepository;
import com.style_haven.order_service.repos.OrderItemRepository;
import com.style_haven.order_service.repos.OrderRepository;
import com.style_haven.order_service.util.NotFoundException;
import com.style_haven.order_service.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(final OrderRepository orderRepository, final CartRepository cartRepository,
            final OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("order"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Integer order) {
        return orderRepository.findById(order)
                .map(orderEntity -> mapToDTO(orderEntity, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getOrder();
    }

    public void update(final Integer order, final OrderDTO orderDTO) {
        final Order orderEntity = orderRepository.findById(order)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, orderEntity);
        orderRepository.save(orderEntity);
    }

    public void delete(final Integer order) {
        orderRepository.deleteById(order);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setOrder(order.getOrder());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCart(order.getCart() == null ? null : order.getCart().getCartId());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setUserId(orderDTO.getUserId());
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(orderDTO.getStatus());
        final Cart cart = orderDTO.getCart() == null ? null : cartRepository.findById(orderDTO.getCart())
                .orElseThrow(() -> new NotFoundException("cart not found"));
        order.setCart(cart);
        return order;
    }

    public ReferencedWarning getReferencedWarning(final Integer order) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Order order = orderRepository.findById(order)
                .orElseThrow(NotFoundException::new);
        final OrderItem orderOrderItem = orderItemRepository.findFirstByOrder(order);
        if (orderOrderItem != null) {
            referencedWarning.setKey("order.orderItem.order.referenced");
            referencedWarning.addParam(orderOrderItem.getOrderItemId());
            return referencedWarning;
        }
        return null;
    }

}

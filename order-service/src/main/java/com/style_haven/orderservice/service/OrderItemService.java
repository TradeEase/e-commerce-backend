package com.style_haven.orderservice.service;

import com.style_haven.orderservice.domain.Order;
import com.style_haven.orderservice.domain.OrderItem;
import com.style_haven.orderservice.model.OrderItemDTO;
import com.style_haven.orderservice.repos.OrderItemRepository;
import com.style_haven.orderservice.repos.OrderRepository;
import com.style_haven.orderservice.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemService(final OrderItemRepository orderItemRepository,
            final OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    public List<OrderItemDTO> findAll() {
        final List<OrderItem> orderItems = orderItemRepository.findAll(Sort.by("orderItemId"));
        return orderItems.stream()
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .toList();
    }

    public OrderItemDTO get(final Integer orderItemId) {
        return orderItemRepository.findById(orderItemId)
                .map(orderItem -> mapToDTO(orderItem, new OrderItemDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = new OrderItem();
        mapToEntity(orderItemDTO, orderItem);
        return orderItemRepository.save(orderItem).getOrderItemId();
    }

    public void update(final Integer orderItemId, final OrderItemDTO orderItemDTO) {
        final OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderItemDTO, orderItem);
        orderItemRepository.save(orderItem);
    }

    public void delete(final Integer orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    private OrderItemDTO mapToDTO(final OrderItem orderItem, final OrderItemDTO orderItemDTO) {
        orderItemDTO.setOrderItemId(orderItem.getOrderItemId());
        orderItemDTO.setProductId(orderItem.getProductId());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        orderItemDTO.setOrder(orderItem.getOrder() == null ? null : orderItem.getOrder().getOrderId());
        return orderItemDTO;
    }

    private OrderItem mapToEntity(final OrderItemDTO orderItemDTO, final OrderItem orderItem) {
        orderItem.setProductId(orderItemDTO.getProductId());
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem.setPrice(orderItemDTO.getPrice());
        final Order order = orderItemDTO.getOrder() == null ? null : orderRepository.findById(orderItemDTO.getOrder())
                .orElseThrow(() -> new NotFoundException("order not found"));
        orderItem.setOrder(order);
        return orderItem;
    }

}

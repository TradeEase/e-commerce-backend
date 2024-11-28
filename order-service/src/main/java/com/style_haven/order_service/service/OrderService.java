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
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final WebClient webClient;

    public OrderService(final OrderRepository orderRepository, final CartRepository cartRepository,
                        final OrderItemRepository orderItemRepository, final WebClient.Builder webClientBuilder) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build(); // Base URL for WebClient
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("orderId"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Integer orderId) {
        return orderRepository.findById(orderId)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getOrderId();
    }

    public void update(final Integer orderId, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Integer orderId) {
        orderRepository.deleteById(orderId);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setOrderId(order.getOrderId());
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
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        order.setCart(cart);
        return order;
    }

    public ReferencedWarning getReferencedWarning(final Integer orderId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(NotFoundException::new);
        final OrderItem orderOrderItem = orderItemRepository.findFirstByOrder(order);
        if (orderOrderItem != null) {
            referencedWarning.setKey("order.orderItem.order.referenced");
            referencedWarning.addParam(orderOrderItem.getOrderItemId());
            return referencedWarning;
        }
        return null;
    }

    public String fetchDataFromOtherService(String productId) {
        String url = "/api/product/products/{productId}";

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class) // Response type
                .block(); // Blocking call (not recommended in reactive applications)
    }
}

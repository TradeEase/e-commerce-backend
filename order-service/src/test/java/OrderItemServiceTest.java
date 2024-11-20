import com.style_haven.order_service.domain.OrderItem;
import com.style_haven.order_service.model.OrderItemDTO;
import com.style_haven.order_service.repos.OrderItemRepository;
import com.style_haven.order_service.service.OrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(1);
        orderItem.setProductId(101);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(20.00));

        when(orderItemRepository.findAll(Sort.by(Sort.Direction.ASC, "orderItemId"))).thenReturn(List.of(orderItem));

        // Act
        List<OrderItemDTO> result = orderItemService.findAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(101, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(20.00), result.get(0).getPrice());
        verify(orderItemRepository, times(1)).findAll(Sort.by(Sort.Direction.ASC, "orderItemId"));
    }

    @Test
    void testCreate() {
        // Arrange
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(101);
        orderItemDTO.setQuantity(3);
        orderItemDTO.setPrice(BigDecimal.valueOf(30.00));

        OrderItem savedOrderItem = new OrderItem();
        savedOrderItem.setOrderItemId(1);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(savedOrderItem);

        // Act
        Integer createdId = orderItemService.create(orderItemDTO);

        // Assert
        assertEquals(1, createdId);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testGet() {
        // Arrange
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(1);
        orderItem.setProductId(101);
        orderItem.setQuantity(2);
        orderItem.setPrice(BigDecimal.valueOf(20.00));

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));

        // Act
        OrderItemDTO result = orderItemService.get(1);

        // Assert
        assertNotNull(result);
        assertEquals(101, result.getProductId());
        assertEquals(2, result.getQuantity());
        assertEquals(BigDecimal.valueOf(20.00), result.getPrice());
        verify(orderItemRepository, times(1)).findById(1);
    }

    @Test
    void testUpdate() {
        // Arrange
        OrderItem existingOrderItem = new OrderItem();
        existingOrderItem.setOrderItemId(1);
        existingOrderItem.setProductId(101);
        existingOrderItem.setQuantity(2);
        existingOrderItem.setPrice(BigDecimal.valueOf(20.00));

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(102);
        orderItemDTO.setQuantity(4);
        orderItemDTO.setPrice(BigDecimal.valueOf(40.00));

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(existingOrderItem);

        // Act
        orderItemService.update(1, orderItemDTO);

        // Assert
        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductId(102);
        orderItemDTO.setQuantity(4);
        orderItemDTO.setPrice(BigDecimal.valueOf(40.00));

        when(orderItemRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderItemService.update(1, orderItemDTO));
        verify(orderItemRepository, times(1)).findById(1);
    }

    @Test
    void testDelete() {
        // Arrange
        when(orderItemRepository.existsById(1)).thenReturn(true);

        // Act
        orderItemService.delete(1);

        // Assert
        verify(orderItemRepository, times(1)).existsById(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(orderItemRepository.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderItemService.delete(1));
        verify(orderItemRepository, times(1)).existsById(1);
    }
}

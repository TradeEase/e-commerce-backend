
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.style_haven.product_service.domain.Product;
import com.style_haven.product_service.model.ProductDTO;
import com.style_haven.product_service.repos.CategoryRepository;
import com.style_haven.product_service.repos.ProductRepository;
import com.style_haven.product_service.repos.ReviewRepository;
import com.style_haven.product_service.util.NotFoundException;
import com.style_haven.product_service.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

class ProductServiceProductTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    @Test
    void testGet_NotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.get(1L));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("New Product");
        productDTO.setPrice(200);

        Product product = new Product();
        product.setProductId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Long productId = productService.create(productDTO);

        // Assert
        assertEquals(1L, productId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdate() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setProductId(1L);
        existingProduct.setName("Existing Product");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(150);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        productService.update(1L, productDTO);

        // Assert
        assertEquals("Updated Product", existingProduct.getName());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.update(1L, productDTO));
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete() {
        // Arrange
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRepository, times(1)).deleteById(1L);
    }

  
}

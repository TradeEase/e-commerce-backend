package com.style_haven.product_service.service;

import com.style_haven.product_service.domain.Category;
import com.style_haven.product_service.domain.Product;
import com.style_haven.product_service.domain.Review;
import com.style_haven.product_service.model.ProductDTO;
import com.style_haven.product_service.repos.CategoryRepository;
import com.style_haven.product_service.repos.ProductRepository;
import com.style_haven.product_service.repos.ReviewRepository;
import com.style_haven.product_service.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(final ProductRepository productRepository,
            final CategoryRepository categoryRepository, final ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("productId"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Long productId) {
        return productRepository.findById(productId)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getProductId();
    }

    public void update(final Long productId, final ProductDTO productDTO) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long productId) {
        productRepository.deleteById(productId);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setProductId(product.getProductId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setImage(product.getImage());
        productDTO.setCategories(product.getCategories().stream()
                .map(Category::getCategoryId)
                .toList());
        productDTO.setReview(product.getReview() == null ? null : product.getReview().getReviewId());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setImage(productDTO.getImage());
        final List<Category> categories = categoryRepository.findAllById(
                productDTO.getCategories() == null ? Collections.emptyList() : productDTO.getCategories());
        if (categories.size() != (productDTO.getCategories() == null ? 0 : productDTO.getCategories().size())) {
            throw new NotFoundException("one of categories not found");
        }
        product.setCategories(new HashSet<>(categories));
        final Review review = productDTO.getReview() == null ? null : reviewRepository.findById(productDTO.getReview())
                .orElseThrow(() -> new NotFoundException("review not found"));
        product.setReview(review);
        return product;
    }

}

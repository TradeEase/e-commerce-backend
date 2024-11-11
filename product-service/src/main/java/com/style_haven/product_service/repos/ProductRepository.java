package com.style_haven.product_service.repos;

import com.style_haven.product_service.domain.Category;
import com.style_haven.product_service.domain.Product;
import com.style_haven.product_service.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByCategories(Category category);

    Product findFirstByReview(Review review);

    List<Product> findAllByCategories(Category category);

}

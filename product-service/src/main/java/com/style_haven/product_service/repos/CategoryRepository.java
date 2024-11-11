package com.style_haven.product_service.repos;

import com.style_haven.product_service.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
}

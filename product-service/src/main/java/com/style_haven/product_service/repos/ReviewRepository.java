package com.style_haven.product_service.repos;

import com.style_haven.product_service.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Integer> {
}

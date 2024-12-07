package com.style_haven.product_service.service;

import com.style_haven.product_service.domain.Product;
import com.style_haven.product_service.domain.Review;
import com.style_haven.product_service.model.ReviewDTO;
import com.style_haven.product_service.repos.ProductRepository;
import com.style_haven.product_service.repos.ReviewRepository;
import com.style_haven.product_service.util.NotFoundException;
import com.style_haven.product_service.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(final ReviewRepository reviewRepository,
                         final ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("reviewId"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final Integer reviewId) {
        return reviewRepository.findById(reviewId)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getReviewId();
    }

    public void update(final Integer reviewId, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setProductId(review.getProductId());
        reviewDTO.setUserId(review.getUserId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setProductId(reviewDTO.getProductId());
        review.setUserId(reviewDTO.getUserId());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        return review;
    }

    public ReferencedWarning getReferencedWarning(final Integer reviewId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);
        final Product reviewProduct = productRepository.findFirstByReview(review);
        if (reviewProduct != null) {
            referencedWarning.setKey("review.product.review.referenced");
            referencedWarning.addParam(reviewProduct.getProductId());
            return referencedWarning;
        }
        return null;
    }

}

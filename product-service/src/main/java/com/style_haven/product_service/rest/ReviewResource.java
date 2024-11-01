package com.style_haven.product_service.rest;

import com.style_haven.product_service.model.ReviewDTO;
import com.style_haven.product_service.service.ReviewService;
import com.style_haven.product_service.util.ReferencedException;
import com.style_haven.product_service.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewResource {

    private final ReviewService reviewService;

    public ReviewResource(final ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.findAll());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(
            @PathVariable(name = "reviewId") final Integer reviewId) {
        return ResponseEntity.ok(reviewService.get(reviewId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createReview(@RequestBody @Valid final ReviewDTO reviewDTO) {
        final Integer createdReviewId = reviewService.create(reviewDTO);
        return new ResponseEntity<>(createdReviewId, HttpStatus.CREATED);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Integer> updateReview(
            @PathVariable(name = "reviewId") final Integer reviewId,
            @RequestBody @Valid final ReviewDTO reviewDTO) {
        reviewService.update(reviewId, reviewDTO);
        return ResponseEntity.ok(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteReview(
            @PathVariable(name = "reviewId") final Integer reviewId) {
        final ReferencedWarning referencedWarning = reviewService.getReferencedWarning(reviewId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        reviewService.delete(reviewId);
        return ResponseEntity.noContent().build();
    }

}

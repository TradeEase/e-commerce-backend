package com.style_haven.product_service.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {

    private Integer reviewId;
    private Integer productId;


    private String userId;
    private Integer rating;
    private String comment;

}

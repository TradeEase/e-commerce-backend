package com.style_haven.product_service.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {

    private Integer reviewId;
    private Integer productId;
    private Integer userId;
    private Integer rating;
    private String comment;

}

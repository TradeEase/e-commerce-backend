package com.style_haven.orderservice.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Integer orderId;

    @Size(max = 255)
    private String userId;

    private Boolean status;

    private Integer productId;

    private Integer quantity;

    private Integer cart;

}

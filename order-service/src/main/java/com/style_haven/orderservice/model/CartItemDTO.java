package com.style_haven.orderservice.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDTO {

    private Integer cartItemId;
    private Integer productId;
    private Integer quantity;
    private Integer cart;

}

package com.style_haven.order_service.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDTO {

    private Integer cartItemId;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private Integer cart;

}

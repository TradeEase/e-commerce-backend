package com.style_haven.order_service.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Integer order;
    private Integer userId;
    private Integer totalAmount;
    private Boolean status;
    private Integer cart;

}

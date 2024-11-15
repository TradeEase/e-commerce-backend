package com.style_haven.order_service.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;
    private Boolean status;
    private Integer cart;

}

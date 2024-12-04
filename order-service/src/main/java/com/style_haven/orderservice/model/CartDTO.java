package com.style_haven.orderservice.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartDTO {

    private Integer cartId;

    @Size(max = 255)
    private String userId;

}

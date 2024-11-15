package com.style_haven.product_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {

    private Long productId;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private Integer price;

    private String description;

    private Integer quantity;

    @Size(max = 255)
    private String image;

    private List<Long> categories;

    private Integer review;

}

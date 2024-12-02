package com.style_haven.product_service.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDTO {

    private Long categoryId;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;

}

package com.style_haven.user_service.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressDTO {

    private Integer addressId;

    private Integer userId;

    private String street;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String state;

    private Integer postalCode;

}

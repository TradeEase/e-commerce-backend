package com.style_haven.payment_gateway_service;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD;
    }
    @Setter
    private String description;
    private int amount; // cents
    @Setter
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;


}
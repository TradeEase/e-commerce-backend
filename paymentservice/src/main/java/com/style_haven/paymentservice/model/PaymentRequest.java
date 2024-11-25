package com.style_haven.paymentservice.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long amount;
    private String description;
    private String currency;
}
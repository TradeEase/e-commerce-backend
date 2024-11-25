package com.style_haven.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String paymentIntentId;
    private String clientSecret;
    private String message;
}
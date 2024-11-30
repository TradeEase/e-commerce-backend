package com.style_haven.paymentservice.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.style_haven.paymentservice.model.PaymentRequest;
import com.style_haven.paymentservice.model.PaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.secret-key}")
    private String stripeApiKey;
//example.payment.dto.PaymentResponse createPayment
    public com.style_haven.paymentservice.model.PaymentResponse createPayment(PaymentRequest paymentRequest) {
        try {
            Stripe.apiKey = stripeApiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentRequest.getAmount() * 100); // Convert to cents
            params.put("currency", "usd");
            params.put("payment_method_types", new String[]{"card"});
            params.put("description", paymentRequest.getDescription());

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return new PaymentResponse(
                    paymentIntent.getId(),
                    paymentIntent.getClientSecret(),
                    "Payment Intent created successfully"
            );
        } catch (StripeException e) {
            throw new RuntimeException("Error creating payment intent", e);
        }
    }

    public PaymentResponse confirmPayment(String paymentIntentId) {
        try {
            Stripe.apiKey = stripeApiKey;

            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            PaymentIntent confirmedPayment = paymentIntent.confirm();

            return new PaymentResponse(
                    confirmedPayment.getId(),
                    confirmedPayment.getClientSecret(),
                    "Payment confirmed successfully"
            );
        } catch (StripeException e) {
            throw new RuntimeException("Error confirming payment", e);
        }
    }
}
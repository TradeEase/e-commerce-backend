package com.style_haven.paymentservice.service;

import com.style_haven.paymentservice.entity.Payment;
import com.style_haven.paymentservice.model.PaymentRequest;
import com.style_haven.paymentservice.model.PaymentResponse;
import com.style_haven.paymentservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.secret-key}")
    private String stripeApiKey;

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        try {
            Stripe.apiKey = stripeApiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("amount", paymentRequest.getAmount() * 100); // Convert to cents
            params.put("currency", "usd");
            params.put("payment_method_types", new String[]{"card"});
            params.put("description", paymentRequest.getDescription());

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Payment payment = new Payment();
            payment.setPaymentIntentId(paymentIntent.getId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency());
            payment.setDescription(paymentRequest.getDescription());
            payment.setStatus("created");
            payment.setClientSecret(paymentIntent.getClientSecret());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

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

            Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId);
            payment.setStatus("confirmed");
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

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

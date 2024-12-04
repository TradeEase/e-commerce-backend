package com.style_haven.paymentservice.rest;

import com.style_haven.paymentservice.model.PaymentRequest;
import com.style_haven.paymentservice.model.PaymentResponse;
import com.style_haven.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.createPayment(paymentRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestParam("paymentIntentId") String paymentIntentId) {
        PaymentResponse response = paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok(response);
    }
}
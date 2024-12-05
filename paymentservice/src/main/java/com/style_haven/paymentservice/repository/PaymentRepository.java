package com.style_haven.paymentservice.repository;

import com.style_haven.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPaymentIntentId(String paymentIntentId);
}

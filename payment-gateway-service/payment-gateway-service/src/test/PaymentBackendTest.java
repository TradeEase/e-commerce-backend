package com.style_haven.payment_gateway_service;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentBackendTest {

    @Mock
    private Model model;

    @Mock
    private StripeService stripeService;

    @InjectMocks
    private ChargeController chargeController;

    private ChargeRequest chargeRequest;

    @BeforeEach
    void setUp() {
        chargeRequest = new ChargeRequest();
        chargeRequest.setAmount(5000);
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        chargeRequest.setStripeToken("tok_visa");
        chargeRequest.setDescription("Test Charge");
    }

    @Test
    void testSuccessfulCharge() throws Exception {
        // Arrange
        Charge mockCharge = mock(Charge.class);
        when(mockCharge.getId()).thenReturn("ch_123456");
        when(mockCharge.getStatus()).thenReturn("succeeded");
        when(mockCharge.getBalanceTransaction()).thenReturn("txn_123456");

        when(stripeService.charge(chargeRequest)).thenReturn(mockCharge);

        // Act
        String result = chargeController.charge(chargeRequest, model);

        // Assert
        assertEquals("result", result);
        verify(model).addAttribute("id", "ch_123456");
        verify(model).addAttribute("status", "succeeded");
        verify(model).addAttribute("chargeId", "ch_123456");
        verify(model).addAttribute("balance_transaction", "txn_123456");
    }

    @Test
    void testChargeWithCardException() throws Exception {
        // Arrange
        CardException cardException = new CardException("Card declined");

        // Act
        String result = chargeController.handleError(model, cardException);

        // Assert
        assertEquals("result", result);
        verify(model).addAttribute("error", "Card declined");
    }

    @Test
    void testCheckoutControllerAttributes() {
        // Arrange
        CheckoutController checkoutController = new CheckoutController();
        checkoutController.stripePublicKey = "pk_test_key";

        // Act
        String viewName = checkoutController.checkout(model);

        // Assert
        assertEquals("checkout", viewName);
        verify(model).addAttribute("amount", 5000);
        verify(model).addAttribute("stripePublicKey", "pk_test_key");
        verify(model).addAttribute("currency", ChargeRequest.Currency.EUR);
    }
}
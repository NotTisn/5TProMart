package com.fivetpromart.domain.model.strategy.payment;

import java.math.BigDecimal;

/**
 * Strategy Pattern for Payment Processing
 * Allows different payment methods to have their own behavior
 */
public interface PaymentStrategy {
    
    /**
     * Process the payment
     * @param amount Amount to be paid
     * @param amountGiven Amount given by customer
     * @return PaymentResult containing change and payment details
     */
    PaymentResult processPayment(BigDecimal amount, BigDecimal amountGiven);
    
    /**
     * Get the payment method name
     * @return Payment method identifier (CASH, BANK_TRANSFER, etc.)
     */
    String getPaymentMethod();
    
    /**
     * Validate if the payment can be processed
     * @param amount Amount to be paid
     * @param amountGiven Amount given by customer
     * @return true if valid, false otherwise
     */
    boolean validate(BigDecimal amount, BigDecimal amountGiven);
}

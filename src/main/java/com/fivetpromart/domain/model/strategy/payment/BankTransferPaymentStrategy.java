package com.fivetpromart.domain.model.strategy.payment;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Bank Transfer Payment Strategy
 * Handles electronic bank transfers
 */
public class BankTransferPaymentStrategy implements PaymentStrategy {
    
    @Override
    public PaymentResult processPayment(BigDecimal amount, BigDecimal amountGiven) {
        if (!validate(amount, amountGiven)) {
            throw new InvalidOrderException("Invalid bank transfer amount");
        }
        
        // For bank transfer, amount given should equal the total (no change)
        String transactionRef = generateTransactionReference();
        
        return PaymentResult.builder()
                .amountPaid(amount)
                .changeReturned(BigDecimal.ZERO)  // No change for bank transfer
                .paymentMethod(getPaymentMethod())
                .successful(true)
                .transactionReference(transactionRef)
                .build();
    }
    
    @Override
    public String getPaymentMethod() {
        return "BANK_TRANSFER";
    }
    
    @Override
    public boolean validate(BigDecimal amount, BigDecimal amountGiven) {
        if (amount == null || amountGiven == null) {
            return false;
        }
        // For bank transfer, amount given should match exactly
        return amountGiven.compareTo(amount) == 0;
    }
    
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

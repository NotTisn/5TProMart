package com.fivetpromart.domain.model.strategy.payment;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;

/**
 * Cash Payment Strategy
 * Handles cash payments with change calculation
 */
public class CashPaymentStrategy implements PaymentStrategy {
    
    @Override
    public PaymentResult processPayment(BigDecimal amount, BigDecimal amountGiven) {
        if (!validate(amount, amountGiven)) {
            throw new InvalidOrderException("Amount given is less than total amount for cash payment");
        }
        
        BigDecimal change = amountGiven.subtract(amount);
        
        return PaymentResult.builder()
                .amountPaid(amount)
                .changeReturned(change)
                .paymentMethod(getPaymentMethod())
                .successful(true)
                .transactionReference(null)  // No reference for cash
                .build();
    }
    
    @Override
    public String getPaymentMethod() {
        return "CASH";
    }
    
    @Override
    public boolean validate(BigDecimal amount, BigDecimal amountGiven) {
        if (amount == null || amountGiven == null) {
            return false;
        }
        // For cash, customer must give at least the total amount
        return amountGiven.compareTo(amount) >= 0;
    }
}

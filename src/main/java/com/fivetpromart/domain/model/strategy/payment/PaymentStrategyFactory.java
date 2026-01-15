package com.fivetpromart.domain.model.strategy.payment;

import com.fivetpromart.domain.exception.InvalidOrderException;

/**
 * Factory for creating payment strategy instances
 * Implements Factory Pattern + Strategy Pattern
 */
public class PaymentStrategyFactory {
    
    /**
     * Create payment strategy based on payment method
     * @param paymentMethod Payment method identifier (CASH, BANK_TRANSFER)
     * @return PaymentStrategy implementation
     */
    public static PaymentStrategy createStrategy(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            throw new InvalidOrderException("Payment method is required");
        }
        
        return switch (paymentMethod.toUpperCase()) {
            case "CASH" -> new CashPaymentStrategy();
            case "BANK_TRANSFER" -> new BankTransferPaymentStrategy();
            default -> throw new InvalidOrderException(
                    "Unsupported payment method: " + paymentMethod + 
                    ". Supported methods: CASH, BANK_TRANSFER"
            );
        };
    }
}

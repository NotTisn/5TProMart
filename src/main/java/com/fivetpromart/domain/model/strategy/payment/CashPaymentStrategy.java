package com.fivetpromart.domain.model.strategy.payment;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cash Payment Strategy
 * Handles cash payments with change calculation and Vietnamese retail rounding.
 * 
 * Vietnam Standard: Round to nearest 1,000 VND
 * - 37,300 → 37,000 (round down)
 * - 37,600 → 38,000 (round up)
 * - 37,500 → 38,000 (midpoint rounds up per HALF_UP)
 */
public class CashPaymentStrategy implements PaymentStrategy {
    
    /**
     * Rounding unit for Vietnamese cash payments (1,000 VND)
     * TODO: Make configurable via application.yml
     */
    private static final BigDecimal ROUNDING_UNIT = new BigDecimal("1000");
    
    /**
     * Round amount to nearest rounding unit.
     * Uses HALF_UP mode: 0.5 rounds up.
     * 
     * @param amount Original amount
     * @return Rounded amount
     */
    private BigDecimal roundToNearest(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        
        // Divide by rounding unit, round to 0 decimal places, multiply back
        // Example: 37,600 / 1000 = 37.6 → rounds to 38 → 38 * 1000 = 38,000
        return amount.divide(ROUNDING_UNIT, 0, RoundingMode.HALF_UP)
                     .multiply(ROUNDING_UNIT);
    }
    
    @Override
    public PaymentResult processPayment(BigDecimal amount, BigDecimal amountGiven) {
        // Round the total amount (Vietnam retail standard)
        BigDecimal roundedAmount = roundToNearest(amount);
        BigDecimal roundingAdjustment = roundedAmount.subtract(amount);
        
        // Validate against ROUNDED amount
        if (!validate(roundedAmount, amountGiven)) {
            throw new InvalidOrderException(
                String.format("Amount given (%.0f VND) is less than rounded total (%.0f VND)",
                    amountGiven, roundedAmount)
            );
        }
        
        BigDecimal change = amountGiven.subtract(roundedAmount);
        
        return PaymentResult.builder()
                .amountPaid(roundedAmount)           // Rounded amount customer pays
                .changeReturned(change)              // Change from rounded amount
                .originalAmount(amount)              // Original subtotal before rounding
                .roundingAdjustment(roundingAdjustment)  // How much we rounded (+/-)
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

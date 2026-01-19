package com.fivetpromart.domain.model.strategy.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Value Object representing the result of a payment transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {
    private BigDecimal amountPaid;
    private BigDecimal changeReturned;
    private String paymentMethod;
    private boolean successful;
    private String transactionReference;  // For bank transfers or e-wallets
    
    // Cash rounding fields (Vietnam retail standard)
    private BigDecimal originalAmount;      // Before rounding (e.g., 37,300)
    private BigDecimal roundingAdjustment;  // +/- adjustment (e.g., -300)
}

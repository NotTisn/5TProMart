package com.fivetpromart.domain.model.strategy.discount;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;

/**
 * Fixed Amount Discount Strategy
 * Applies a fixed discount amount to the subtotal
 */
public class FixedAmountDiscountStrategy implements DiscountStrategy {
    
    private final BigDecimal discountAmount;
    
    /**
     * Constructor
     * @param discountAmount Fixed discount amount
     */
    public FixedAmountDiscountStrategy(BigDecimal discountAmount) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOrderException("Discount amount must be non-negative");
        }
        this.discountAmount = discountAmount;
    }
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal subTotal) {
        if (!isValid(subTotal)) {
            return BigDecimal.ZERO;
        }
        
        // Discount cannot exceed subtotal
        if (discountAmount.compareTo(subTotal) > 0) {
            return subTotal;
        }
        
        return discountAmount;
    }
    
    @Override
    public String getDescription() {
        return String.format("Fixed discount: %,.0f VND", discountAmount);
    }
    
    @Override
    public String getDiscountType() {
        return "FIXED_AMOUNT";
    }
    
    @Override
    public boolean isValid(BigDecimal subTotal) {
        return subTotal != null && subTotal.compareTo(BigDecimal.ZERO) > 0;
    }
}

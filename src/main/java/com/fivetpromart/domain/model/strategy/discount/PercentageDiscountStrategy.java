package com.fivetpromart.domain.model.strategy.discount;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Percentage Discount Strategy
 * Applies a percentage discount to the subtotal
 */
public class PercentageDiscountStrategy implements DiscountStrategy {
    
    private final BigDecimal percentage;
    private final BigDecimal maxDiscount;  // Optional maximum discount cap
    
    /**
     * Constructor with percentage only
     * @param percentage Discount percentage (e.g., 10 for 10%)
     */
    public PercentageDiscountStrategy(BigDecimal percentage) {
        this(percentage, null);
    }
    
    /**
     * Constructor with percentage and max discount cap
     * @param percentage Discount percentage (e.g., 10 for 10%)
     * @param maxDiscount Maximum discount amount (optional)
     */
    public PercentageDiscountStrategy(BigDecimal percentage, BigDecimal maxDiscount) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0 || 
            percentage.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new InvalidOrderException("Discount percentage must be between 0 and 100");
        }
        this.percentage = percentage;
        this.maxDiscount = maxDiscount;
    }
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal subTotal) {
        if (!isValid(subTotal)) {
            return BigDecimal.ZERO;
        }
        
        // Calculate percentage discount
        BigDecimal discount = subTotal
                .multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        // Apply max discount cap if specified
        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            return maxDiscount;
        }
        
        return discount;
    }
    
    @Override
    public String getDescription() {
        String desc = String.format("%.0f%% discount", percentage);
        if (maxDiscount != null) {
            desc += String.format(" (max: %,.0f VND)", maxDiscount);
        }
        return desc;
    }
    
    @Override
    public String getDiscountType() {
        return "PERCENTAGE";
    }
    
    @Override
    public boolean isValid(BigDecimal subTotal) {
        return subTotal != null && subTotal.compareTo(BigDecimal.ZERO) > 0;
    }
}

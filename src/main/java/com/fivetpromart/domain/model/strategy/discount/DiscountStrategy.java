package com.fivetpromart.domain.model.strategy.discount;

import java.math.BigDecimal;

/**
 * Strategy Pattern for Discount Calculation
 * Allows different discount types to have their own calculation logic
 */
public interface DiscountStrategy {
    
    /**
     * Calculate discount amount based on subtotal
     * @param subTotal The subtotal before discount
     * @return Discount amount to be deducted
     */
    BigDecimal calculateDiscount(BigDecimal subTotal);
    
    /**
     * Get discount description for display
     * @return Human-readable discount description
     */
    String getDescription();
    
    /**
     * Get discount type identifier
     * @return Discount type (PERCENTAGE, FIXED_AMOUNT, LOYALTY_POINTS, etc.)
     */
    String getDiscountType();
    
    /**
     * Validate if discount can be applied
     * @param subTotal The subtotal to validate against
     * @return true if discount is valid, false otherwise
     */
    boolean isValid(BigDecimal subTotal);
}

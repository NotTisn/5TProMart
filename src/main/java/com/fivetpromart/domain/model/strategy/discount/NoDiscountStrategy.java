package com.fivetpromart.domain.model.strategy.discount;

import java.math.BigDecimal;

/**
 * No Discount Strategy (Null Object Pattern)
 * Used when no discount is applied
 */
public class NoDiscountStrategy implements DiscountStrategy {
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal subTotal) {
        return BigDecimal.ZERO;
    }
    
    @Override
    public String getDescription() {
        return "No discount applied";
    }
    
    @Override
    public String getDiscountType() {
        return "NONE";
    }
    
    @Override
    public boolean isValid(BigDecimal subTotal) {
        return true;  // Always valid
    }
}

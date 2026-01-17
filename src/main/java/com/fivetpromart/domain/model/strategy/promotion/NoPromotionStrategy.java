package com.fivetpromart.domain.model.strategy.promotion;

import java.math.BigDecimal;

/**
 * No Promotion Strategy (Null Object Pattern)
 * Used when no promotion is applied
 */
public class NoPromotionStrategy implements PromotionStrategy {
    
    @Override
    public BigDecimal calculatePromotionalPrice(BigDecimal originalPrice, int quantity) {
        return originalPrice;  // No discount applied
    }
    
    @Override
    public String getDescription() {
        return "No promotion applied";
    }
    
    @Override
    public String getPromotionType() {
        return "NONE";
    }
    
    @Override
    public boolean isValid(BigDecimal originalPrice, int quantity) {
        return true;  // Always valid (no restrictions)
    }
}

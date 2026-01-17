package com.fivetpromart.domain.model.strategy.promotion;

import java.math.BigDecimal;

/**
 * Strategy Pattern for Promotion Calculation
 * Allows different promotion types to have their own calculation logic
 */
public interface PromotionStrategy {
    
    /**
     * Calculate promotional price for a product
     * @param originalPrice The original selling price
     * @param quantity The quantity being purchased
     * @return Promotional price after applying the promotion
     */
    BigDecimal calculatePromotionalPrice(BigDecimal originalPrice, int quantity);
    
    /**
     * Get promotion description for display
     * @return Human-readable promotion description
     */
    String getDescription();
    
    /**
     * Get promotion type identifier
     * @return Promotion type (DISCOUNT, BUY_X_GET_Y, etc.)
     */
    String getPromotionType();
    
    /**
     * Validate if promotion can be applied
     * @param originalPrice The price to validate against
     * @param quantity The quantity to validate
     * @return true if promotion is valid, false otherwise
     */
    boolean isValid(BigDecimal originalPrice, int quantity);
}

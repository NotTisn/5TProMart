package com.fivetpromart.domain.model.strategy.promotion;

import com.fivetpromart.domain.exception.InvalidPromotionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Discount Promotion Strategy
 * Applies a percentage discount to product price
 */
public class DiscountPromotionStrategy implements PromotionStrategy {
    
    private final Integer discountPercent;
    
    /**
     * Constructor with discount percentage
     * @param discountPercent Discount percentage (1-100)
     */
    public DiscountPromotionStrategy(Integer discountPercent) {
        if (discountPercent == null || discountPercent < 1 || discountPercent > 100) {
            throw new InvalidPromotionException("Discount percent must be between 1 and 100");
        }
        this.discountPercent = discountPercent;
    }
    
    @Override
    public BigDecimal calculatePromotionalPrice(BigDecimal originalPrice, int quantity) {
        if (!isValid(originalPrice, quantity)) {
            return originalPrice;
        }
        
        // Calculate discounted price: price - (price * percent / 100)
        BigDecimal discountAmount = originalPrice
                .multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        
        BigDecimal promotionalPrice = originalPrice.subtract(discountAmount);
        
        // Ensure price doesn't go negative
        return promotionalPrice.max(BigDecimal.ZERO);
    }
    
    @Override
    public String getDescription() {
        return String.format("%d%% off", discountPercent);
    }
    
    @Override
    public String getPromotionType() {
        return "DISCOUNT";
    }
    
    @Override
    public boolean isValid(BigDecimal originalPrice, int quantity) {
        return originalPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public Integer getDiscountPercent() {
        return discountPercent;
    }
}

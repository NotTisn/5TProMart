package com.fivetpromart.domain.model.strategy.discount;

import com.fivetpromart.domain.exception.InvalidOrderException;

import java.math.BigDecimal;

/**
 * Loyalty Points Discount Strategy
 * Converts loyalty points to discount amount
 * Typically: 1 point = 1 VND discount
 */
public class LoyaltyPointsDiscountStrategy implements DiscountStrategy {
    
    private final Long pointsToUse;
    private final BigDecimal conversionRate;  // Points to VND rate (default 1:1)
    
    /**
     * Constructor with default conversion rate (1 point = 1 VND)
     * @param pointsToUse Number of loyalty points to use
     */
    public LoyaltyPointsDiscountStrategy(Long pointsToUse) {
        this(pointsToUse, BigDecimal.ONE);
    }
    
    /**
     * Constructor with custom conversion rate
     * @param pointsToUse Number of loyalty points to use
     * @param conversionRate Points to VND conversion rate
     */
    public LoyaltyPointsDiscountStrategy(Long pointsToUse, BigDecimal conversionRate) {
        if (pointsToUse == null || pointsToUse < 0) {
            throw new InvalidOrderException("Loyalty points must be non-negative");
        }
        if (conversionRate == null || conversionRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("Conversion rate must be positive");
        }
        this.pointsToUse = pointsToUse;
        this.conversionRate = conversionRate;
    }
    
    @Override
    public BigDecimal calculateDiscount(BigDecimal subTotal) {
        if (!isValid(subTotal)) {
            return BigDecimal.ZERO;
        }
        
        // Convert points to discount amount
        BigDecimal discountAmount = BigDecimal.valueOf(pointsToUse).multiply(conversionRate);
        
        // Discount cannot exceed subtotal
        if (discountAmount.compareTo(subTotal) > 0) {
            return subTotal;
        }
        
        return discountAmount;
    }
    
    @Override
    public String getDescription() {
        return String.format("Loyalty points discount: %d points = %,.0f VND", 
                pointsToUse, 
                BigDecimal.valueOf(pointsToUse).multiply(conversionRate));
    }
    
    @Override
    public String getDiscountType() {
        return "LOYALTY_POINTS";
    }
    
    @Override
    public boolean isValid(BigDecimal subTotal) {
        return subTotal != null && subTotal.compareTo(BigDecimal.ZERO) > 0 && pointsToUse > 0;
    }
    
    /**
     * Get the number of points being used
     * @return Points to use
     */
    public Long getPointsToUse() {
        return pointsToUse;
    }
}

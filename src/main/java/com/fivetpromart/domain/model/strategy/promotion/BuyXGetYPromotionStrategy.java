package com.fivetpromart.domain.model.strategy.promotion;

import com.fivetpromart.domain.exception.InvalidPromotionException;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Buy X Get Y Promotion Strategy
 * Buy X items and get Y items free
 */
public class BuyXGetYPromotionStrategy implements PromotionStrategy {
    
    private final Integer buyQuantity;
    private final Integer getQuantity;
    
    /**
     * Constructor with buy and get quantities
     * @param buyQuantity Number of items to buy
     * @param getQuantity Number of items to get free
     */
    public BuyXGetYPromotionStrategy(Integer buyQuantity, Integer getQuantity) {
        if (buyQuantity == null || buyQuantity <= 0) {
            throw new InvalidPromotionException("Buy quantity must be greater than 0");
        }
        if (getQuantity == null || getQuantity <= 0) {
            throw new InvalidPromotionException("Get quantity must be greater than 0");
        }
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
    }
    
    @Override
    public BigDecimal calculatePromotionalPrice(BigDecimal originalPrice, int quantity) {
        if (!isValid(originalPrice, quantity)) {
            return originalPrice;
        }
        
        // Calculate effective discount based on buy X get Y
        // Example: Buy 2 Get 1 means every 3 items, you only pay for 2
        int bundleSize = buyQuantity + getQuantity;
        int completeBundles = quantity / bundleSize;
        int remainingItems = quantity % bundleSize;
        
        // Calculate total items to pay for
        int itemsToPay = (completeBundles * buyQuantity) + Math.min(remainingItems, buyQuantity);
        
        // Calculate promotional price based on items actually paid for
        BigDecimal totalPrice = originalPrice.multiply(BigDecimal.valueOf(itemsToPay));
        BigDecimal promotionalPrice = totalPrice.divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP);
        
        return promotionalPrice;
    }
    
    @Override
    public String getDescription() {
        return String.format("Buy %d Get %d Free", buyQuantity, getQuantity);
    }
    
    @Override
    public String getPromotionType() {
        return "BUY_X_GET_Y";
    }
    
    @Override
    public boolean isValid(BigDecimal originalPrice, int quantity) {
        // Promotion only applies if quantity meets minimum buy requirement
        return originalPrice != null && 
               originalPrice.compareTo(BigDecimal.ZERO) > 0 && 
               quantity >= buyQuantity;
    }
    
    public Integer getBuyQuantity() {
        return buyQuantity;
    }
    
    public Integer getGetQuantity() {
        return getQuantity;
    }
}

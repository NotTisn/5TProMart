package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InvalidDateRangeException;
import com.fivetpromart.domain.exception.InvalidPromotionException;
import com.fivetpromart.domain.model.strategy.promotion.BuyXGetYPromotionStrategy;
import com.fivetpromart.domain.model.strategy.promotion.DiscountPromotionStrategy;
import com.fivetpromart.domain.model.strategy.promotion.NoPromotionStrategy;
import com.fivetpromart.domain.model.strategy.promotion.PromotionStrategy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion {
    private String promotionId;
    private String promotionName;
    private String promotionDescription;
    private List<PromotionProduct> products;
    private String promotionType;
    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private PromotionStrategy promotionStrategy;

    public static Promotion create(
            String promotionName,
            String promotionDescription,
            List<PromotionProduct> products,
            String promotionType,
            Integer discountPercent,
            Integer buyQuantity,
            Integer getQuantity,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (promotionName == null || promotionName.isBlank()) {
            throw new EmptyFieldException("Promotion name");
        }
        if (products == null || products.isEmpty()) {
            throw new EmptyFieldException("Products");
        }
        if (promotionType == null || promotionType.isBlank()) {
            throw new EmptyFieldException("Promotion type");
        }
        if (startDate == null) {
            throw new EmptyFieldException("Start date");
        }
        if (endDate == null) {
            throw new EmptyFieldException("End date");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException();
        }

        // Validate and create strategy based on promotion type
        PromotionStrategy strategy = createStrategy(promotionType, discountPercent, buyQuantity, getQuantity);

        Promotion promotion = new Promotion();
        promotion.promotionId = UUID.randomUUID().toString();
        promotion.promotionName = promotionName;
        promotion.promotionDescription = promotionDescription;
        promotion.products = products;
        promotion.promotionType = promotionType;
        promotion.discountPercent = discountPercent;
        promotion.buyQuantity = buyQuantity;
        promotion.getQuantity = getQuantity;
        promotion.startDate = startDate;
        promotion.endDate = endDate;
        promotion.status = determineStatus(startDate, endDate);
        promotion.promotionStrategy = strategy;

        return promotion;
    }

    public static Promotion reconstitute(
            String promotionId,
            String promotionName,
            String promotionDescription,
            List<PromotionProduct> products,
            String promotionType,
            Integer discountPercent,
            Integer buyQuantity,
            Integer getQuantity,
            LocalDate startDate,
            LocalDate endDate,
            String status
    ) {
        Promotion promotion = new Promotion();
        promotion.promotionId = promotionId;
        promotion.promotionName = promotionName;
        promotion.promotionDescription = promotionDescription;
        promotion.products = products != null ? products : new ArrayList<>();
        promotion.promotionType = promotionType;
        promotion.discountPercent = discountPercent;
        promotion.buyQuantity = buyQuantity;
        promotion.getQuantity = getQuantity;
        promotion.startDate = startDate;
        promotion.endDate = endDate;
        promotion.status = status;
        
        // Recreate strategy from persisted data
        promotion.promotionStrategy = createStrategy(promotionType, discountPercent, buyQuantity, getQuantity);
        
        return promotion;
    }

    public void cancel() {
        this.status = "Cancelled";
    }

    private static String determineStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) {
            return "Upcoming";
        } else if (now.isAfter(endDate)) {
            return "Expired";
        } else {
            return "Active";
        }
    }
    
    /**
     * Factory method to create appropriate promotion strategy
     */
    private static PromotionStrategy createStrategy(String promotionType, Integer discountPercent, 
                                                     Integer buyQuantity, Integer getQuantity) {
        if (promotionType == null) {
            return new NoPromotionStrategy();
        }
        
        return switch (promotionType) {
            case "Discount" -> {
                if (discountPercent == null) {
                    throw new InvalidPromotionException("Discount percent is required for Discount promotion");
                }
                yield new DiscountPromotionStrategy(discountPercent);
            }
            case "Buy X Get Y" -> {
                if (buyQuantity == null || getQuantity == null) {
                    throw new InvalidPromotionException("Buy quantity and get quantity are required for Buy X Get Y promotion");
                }
                yield new BuyXGetYPromotionStrategy(buyQuantity, getQuantity);
            }
            default -> throw new InvalidPromotionException("Unknown promotion type: " + promotionType);
        };
    }
}

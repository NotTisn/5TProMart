package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckProductResponse {
    private String lotId;
    private String productId;
    private String productName;
    private String unitOfMeasure;
    private BigDecimal unitPrice;
    private Long quantity;
    private BigDecimal subTotal;
    private Long currentStock;
    private String status;
    
    // Promotion info (null if no active promotion)
    private PromotionInfo promotion;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionInfo {
        private String promotionId;
        private String promotionName;
        private String promotionType; // "Discount" or "Buy X Get Y"
        private Integer discountPercent; // for Discount type
        private Integer buyQuantity; // for Buy X Get Y
        private Integer getQuantity; // for Buy X Get Y
        private BigDecimal promotionalPrice; // unitPrice after discount
        private BigDecimal savings; // unitPrice - promotionalPrice
    }
}

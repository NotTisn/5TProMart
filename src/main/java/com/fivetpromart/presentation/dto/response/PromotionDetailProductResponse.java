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
public class PromotionDetailProductResponse {
    private String productId;
    private String productName;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;
    private BigDecimal promotionPrice;
}

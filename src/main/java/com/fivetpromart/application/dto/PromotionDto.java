package com.fivetpromart.application.dto;

import com.fivetpromart.domain.model.strategy.promotion.PromotionStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionDto {
    private String promotionId;
    private String promotionName;
    private String promotionDescription;
    private List<PromotionProductDto> products;
    private String promotionType;
    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private PromotionStrategy promotionStrategy;
}

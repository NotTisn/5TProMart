package com.fivetpromart.presentation.dto.response;

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
public class PromotionResponse {
    private String promotionId;
    private String promotionName;
    private String promotionDescription;
    private List<PromotionProductResponse> products;
    private String promotionType;
    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}

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
public class PromotionDetailResponse {
    private String promotionId;
    private String promotionName;
    private String promotionDescription;
    private String promotionType;
    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PromotionDetailProductResponse> products;
}

package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PromotionCreationCommand {
    private String promotionName;
    private String promotionDescription;
    private List<String> products;
    private String promotionType;
    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;
    private LocalDate startDate;
    private LocalDate endDate;
}

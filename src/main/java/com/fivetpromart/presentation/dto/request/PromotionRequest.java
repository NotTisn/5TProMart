package com.fivetpromart.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PromotionRequest {
    @NotBlank(message = "Promotion name is required.")
    private String promotionName;

    private String promotionDescription;

    @NotEmpty(message = "Products list is required.")
    private List<String> products;

    @NotBlank(message = "Promotion type is required.")
    private String promotionType;

    private Integer discountPercent;
    private Integer buyQuantity;
    private Integer getQuantity;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;
}

package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckProductResultDto {
    private String lotId;
    private String productId;
    private String productName;
    private String unitOfMeasure;
    private BigDecimal unitPrice;
    private Long quantity;
    private BigDecimal subTotal;
    private Long currentStock;
    private String status;
}

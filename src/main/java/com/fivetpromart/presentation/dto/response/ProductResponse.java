package com.fivetpromart.presentation.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private String productId;
    private String productName;
    private String categoryId;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;
    private Integer totalStockQuantity;
}

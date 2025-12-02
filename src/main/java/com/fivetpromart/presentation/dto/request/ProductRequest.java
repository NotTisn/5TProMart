package com.fivetpromart.presentation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductRequest {
    private String categoryId;
    private String productName;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;
}

package com.fivetpromart.application.dto.command;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductCreationCommand {
    private String productName;
    private String categoryId;
    private String unitOfMeasure;
    private BigDecimal sellingPrice;
}

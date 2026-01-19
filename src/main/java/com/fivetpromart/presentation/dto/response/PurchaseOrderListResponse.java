package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PurchaseOrderListResponse {
    private String id;
    private String poCode;
    private String supplierName;
    private String staffNameCreated;
    private BigDecimal totalAmount;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate purchaseDate;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate checkDate;
}

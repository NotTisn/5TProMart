package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PurchaseOrderCreationResponse {
    private String id;
    private String poCode;
    private String supplierName;
    private String status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate purchaseDate;
}

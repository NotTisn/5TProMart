package com.fivetpromart.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderSearchQuery {
    private String search; // Filter by poCode or supplierName
    private String supplierId;
    private String status; // Draft, Completed, Cancelled
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortBy;
    private String order;
}

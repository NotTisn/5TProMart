package com.fivetpromart.application.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchQuery {
    private String search;          // orderId, customerName, customerId
    private String staffId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String paymentMethod;   // "CASH", "BANK_TRANSFER"
    private String status;          // "PAID", "PENDING", "CANCELLED"
    private Pageable pageable;
}

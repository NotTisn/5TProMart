package com.fivetpromart.domain.model.statistics;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class OrderData {
    private LocalDate date;
    private Integer completedOrders;
}

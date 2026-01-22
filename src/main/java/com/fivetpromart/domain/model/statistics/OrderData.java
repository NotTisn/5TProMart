package com.fivetpromart.domain.model.statistics;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class OrderData {
    private Instant date;
    private Integer completedOrders;
}

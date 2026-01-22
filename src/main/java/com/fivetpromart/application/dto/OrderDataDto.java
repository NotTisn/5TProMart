package com.fivetpromart.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OrderDataDto {
    private Instant date;
    private Integer completedOrders;
}

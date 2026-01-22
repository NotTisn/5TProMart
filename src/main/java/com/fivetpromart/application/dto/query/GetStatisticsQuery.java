package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
public class GetStatisticsQuery {
    private Instant startDate;
    private Instant endDate;
    private Integer limit;
}

package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class GetStatisticsQuery {
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer limit;
}

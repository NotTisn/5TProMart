package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PromotionSearchQuery {
    private String search;
    private String type;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String sortBy;
    private String order;
}

package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class OrderDataResponse {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private Integer completedOrders;
}

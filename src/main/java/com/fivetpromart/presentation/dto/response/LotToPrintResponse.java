package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LotToPrintResponse {
    private String lotId;
    private String productName;
    private Long quantity;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;

    private String notes;
}

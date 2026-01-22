package com.fivetpromart.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class InvoiceResponse {
    private String invoiceNumber;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate invoiceDate;

    private List<String> images;
}

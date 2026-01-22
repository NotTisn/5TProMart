package com.fivetpromart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposalBatchResultDto {
    private String disposalId;
    private String staffId;
    private LocalDateTime date;
    private Long totalItems;
}

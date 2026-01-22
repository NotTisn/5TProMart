package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposalBatchResponse {
    private String disposalId;
    private String staffId;
    private String date; // Formatted as "dd-MM-yyyy HH:mm:ss"
    private Long totalItems;
}

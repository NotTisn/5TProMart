package com.fivetpromart.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposalBatchRequest {
    
    @NotNull(message = "Reason is required")
    private String reason;
    
    private String note;
    
    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<DisposalItemRequest> items;
    
    private List<String> image; // URLs of disposal images
}

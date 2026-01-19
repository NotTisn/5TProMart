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
public class CancelOrderResultDto {
    private String orderId;
    private String status;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private String reason;
    private Boolean stockRestored;
}

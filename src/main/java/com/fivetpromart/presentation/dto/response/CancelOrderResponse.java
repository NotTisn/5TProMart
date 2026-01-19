package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderResponse {
    private String orderId;
    private String status;
    private String cancelledAt;
    private String cancelledBy;
    private String reason;
    private Boolean stockRestored;
}

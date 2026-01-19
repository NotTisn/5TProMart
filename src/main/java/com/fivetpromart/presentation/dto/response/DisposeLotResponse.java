package com.fivetpromart.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisposeLotResponse {
    private String disposalId;
    private String lotId;
    private String productId;
    private String productName;
    private Long quantityDisposed;
    private Long remainingLotQuantity;
    private Long productTotalStock;
    private String disposedAt;
    private String disposedBy;
    private String reason;
    private String notes;
}

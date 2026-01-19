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
public class DisposeLotResultDto {
    private String disposalId;
    private String lotId;
    private String productId;
    private String productName;
    private Long quantityDisposed;
    private Long remainingLotQuantity;
    private Long productTotalStock;
    private LocalDateTime disposedAt;
    private String disposedBy;
    private String reason;
    private String notes;
}

package com.fivetpromart.application.dto.response;

import java.time.LocalDateTime;

public record StockReservationDto(
        String reservationId,
        String lotId,
        String productId,
        Long quantity, // FIXED: Use Long to match StockInventory
        String reservedBy,
        LocalDateTime reservedAt,
        LocalDateTime expiresAt,
        String status
) {
}

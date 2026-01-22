package com.fivetpromart.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Request to release multiple reservations at once.
 * Used by browser beforeunload to cleanup zombie reservations.
 */
public record ReleaseBatchReservationsRequest(
        @NotEmpty(message = "Reservation IDs list cannot be empty")
        List<String> reservationIds,
        
        String reason
) {
}

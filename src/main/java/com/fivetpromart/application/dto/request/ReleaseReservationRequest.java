package com.fivetpromart.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReleaseReservationRequest(
        @NotBlank(message = "Reservation ID is required")
        String reservationId,
        
        String reason
) {
}

package com.fivetpromart.presentation;

import com.fivetpromart.application.dto.request.ReleaseBatchReservationsRequest;
import com.fivetpromart.application.dto.request.ReleaseReservationRequest;
import com.fivetpromart.application.dto.request.ReserveStockRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse; // FIXED: Correct package
import com.fivetpromart.application.dto.response.StockReservationDto;
import com.fivetpromart.application.usecase.StockReservationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API for managing stock reservations during POS operations
 * Prevents overselling by reserving stock when products are scanned
 */
@RestController
@RequestMapping("/api/v1/stock-reservations")
@RequiredArgsConstructor
public class StockReservationController {
    
    private final StockReservationUseCase stockReservationUseCase;
    
    /**
     * Reserve stock when product is scanned in POS
     * POST /api/v1/stock-reservations
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StockReservationDto>> reserveStock(
            @RequestBody @Valid ReserveStockRequest request) {
        
        var reservation = stockReservationUseCase.reserveStock(request);
        
        return ResponseEntity
                .status(201)
                .body(ApiResponse.success(reservation, "Stock reserved successfully")); // FIXED: Swap params
    }
    
    /**
     * Release reservation (manual cancellation)
     * POST /api/v1/stock-reservations/release
     */
    @PostMapping("/release")
    public ResponseEntity<ApiResponse<String>> releaseReservation( // FIXED: Change Void to String
            @RequestBody @Valid ReleaseReservationRequest request) {
        
        stockReservationUseCase.releaseReservation(request);
        
        return ResponseEntity
                .ok(ApiResponse.success("Reservation released successfully")); // FIXED: Use correct method
    }

    /**
     * Release multiple reservations (best-effort)
     * POST /api/v1/stock-reservations/release-batch
     */
    @PostMapping("/release-batch")
    public ResponseEntity<ApiResponse<String>> releaseReservationsBatch(
            @RequestBody @Valid ReleaseBatchReservationsRequest request) {

        stockReservationUseCase.releaseReservationsBatch(request);

        return ResponseEntity
                .ok(ApiResponse.success("Reservations released successfully"));
    }
}

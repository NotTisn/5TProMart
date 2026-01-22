package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.request.ReserveStockRequest;
import com.fivetpromart.application.dto.request.ReleaseBatchReservationsRequest;
import com.fivetpromart.application.dto.request.ReleaseReservationRequest;
import com.fivetpromart.application.dto.response.StockReservationDto;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.application.port.out.IStockReservationRepository;
import com.fivetpromart.domain.exception.InsufficientStockException;
import com.fivetpromart.domain.exception.InvalidStockReservationException;
import com.fivetpromart.domain.exception.StockInventoryNotFoundException;
import com.fivetpromart.domain.model.StockReservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockReservationUseCase {
    
    private final IStockReservationRepository reservationRepository;
    private final IStockInventoryRepository stockInventoryRepository;
    
    /**
     * Reserve stock when product is scanned in POS
     */
    @Transactional
    public StockReservationDto reserveStock(ReserveStockRequest request) {
        // Find lot with pessimistic write lock to prevent race conditions
        var lot = stockInventoryRepository.findByIdForUpdate(request.lotId())
                .orElseThrow(() -> new StockInventoryNotFoundException(request.lotId()));
        
        // Check available quantity (total - reserved)
        if (lot.getAvailableQuantity() < request.quantity()) {
            throw new InsufficientStockException(
                    lot.getProductId(), 
                    request.quantity(), 
                    lot.getAvailableQuantity()
            );
        }
        
        // Reserve in domain
        lot.reserveStock(request.quantity());
        
        // Create reservation record
        var reservation = StockReservation.create(
                request.lotId(),
                lot.getProductId(),
                request.quantity(),
                request.reservedBy()
        );
        
        // Persist both
        stockInventoryRepository.save(lot);
        var saved = reservationRepository.save(reservation);
        
        log.info("Reserved {} units from lot {} for {}", 
                request.quantity(), request.lotId(), request.reservedBy());
        
        return toDto(saved);
    }
    
    /**
     * Commit reservations when order is completed
     */
    @Transactional
    public void commitReservations(List<String> reservationIds, String orderId) {
        for (String reservationId : reservationIds) {
            var reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new InvalidStockReservationException(
                            "Reservation not found: " + reservationId));
            
            // Skip if already expired
            if (reservation.isExpired()) {
                log.warn("Reservation {} expired, skipping commit", reservationId);
                continue;
            }
            
            // Commit in domain
            reservation.commit(orderId);
            
            // Commit in inventory (reduces both reserved and total)
            var lot = stockInventoryRepository.findById(reservation.getLotId())
                    .orElseThrow(() -> new StockInventoryNotFoundException(reservation.getLotId()));
            
            lot.commitReservedStock(reservation.getQuantity());
            
            // Persist both
            reservationRepository.save(reservation);
            stockInventoryRepository.save(lot);
            
            log.info("Committed reservation {} for order {}", reservationId, orderId);
        }
    }
    
    /**
     * Release reservation (manual cancellation or timeout)
     */
    @Transactional
    public void releaseReservation(ReleaseReservationRequest request) {
        var reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new InvalidStockReservationException(
                        "Reservation not found: " + request.reservationId()));
        
        if (!reservation.isActiveAndValid()) {
            log.warn("Reservation {} not active, skipping release", request.reservationId());
            return;
        }
        
        // Release in domain
        reservation.release(request.reason());
        
        // Release in inventory
        var lot = stockInventoryRepository.findById(reservation.getLotId())
                .orElseThrow(() -> new StockInventoryNotFoundException(reservation.getLotId()));
        
        lot.releaseStock(reservation.getQuantity());
        
        // Persist both
        reservationRepository.save(reservation);
        stockInventoryRepository.save(lot);
        
        log.info("Released reservation {}: {}", request.reservationId(), request.reason());
    }

    /**
     * Release multiple reservations (best-effort)
     * Used by browser unload cleanup to avoid zombie reservations.
     */
    @Transactional
    public void releaseReservationsBatch(ReleaseBatchReservationsRequest request) {
        for (String reservationId : request.reservationIds()) {
            try {
                releaseReservation(new ReleaseReservationRequest(reservationId, request.reason()));
            } catch (Exception e) {
                log.warn("Failed to release reservation {} in batch: {}", reservationId, e.getMessage());
            }
        }
    }
    
    /**
     * Called by scheduled job to auto-expire reservations
     */
    @Transactional
    public void expireOldReservations() {
        var expired = reservationRepository.findExpired(LocalDateTime.now());
        
        log.info("Found {} expired reservations to clean up", expired.size());
        
        for (var reservation : expired) {
            try {
                // Expire in domain
                reservation.expire();
                
                // Release in inventory
                var lot = stockInventoryRepository.findById(reservation.getLotId())
                        .orElseThrow(() -> new StockInventoryNotFoundException(reservation.getLotId()));
                
                lot.releaseStock(reservation.getQuantity());
                
                // Persist both
                reservationRepository.save(reservation);
                stockInventoryRepository.save(lot);
                
                log.info("Auto-expired reservation {}", reservation.getReservationId());
            } catch (Exception e) {
                log.error("Failed to expire reservation {}: {}", 
                        reservation.getReservationId(), e.getMessage());
            }
        }
    }
    
    private StockReservationDto toDto(StockReservation reservation) {
        return new StockReservationDto(
                reservation.getReservationId(),
                reservation.getLotId(),
                reservation.getProductId(),
                reservation.getQuantity(),
                reservation.getReservedBy(),
                reservation.getReservedAt(),
                reservation.getExpiresAt(),
                reservation.getStatus().name()
        );
    }
}

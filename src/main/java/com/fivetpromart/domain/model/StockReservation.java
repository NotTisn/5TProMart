package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.InvalidStockReservationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Stock Reservation Domain Model
 * 
 * Prevents overselling by reserving stock during checkout process.
 * Vietnam retail pattern: Reserve stock when scanning products,
 * release if not completed within timeout (default 15 minutes).
 * 
 * Lifecycle:
 * 1. ACTIVE - Stock reserved, timer running
 * 2. COMMITTED - Order completed, stock deducted
 * 3. RELEASED - Timeout/cancellation, stock returned
 * 4. EXPIRED - Auto-released by background job
 */
@Slf4j
@Getter
public class StockReservation {
    
    private String reservationId;
    private String lotId;
    private String productId;
    private Long quantity; // FIXED: Use Long to match StockInventory
    private String reservedBy;  // Session ID or staff ID
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;
    
    @Setter(AccessLevel.PACKAGE)
    private ReservationStatus status;
    
    private String orderId;  // Set when committed
    private LocalDateTime committedAt;
    private LocalDateTime releasedAt;
    
    // Default expiry duration (Vietnam retail: 15 minutes)
    private static final Duration DEFAULT_EXPIRY = Duration.ofMinutes(15);
    
    private StockReservation() {}
    
    // =================================================================
    // FACTORY: CREATE
    // =================================================================
    
    /**
     * Create stock reservation with default 15-minute expiry
     */
    public static StockReservation create(
            String lotId,
            String productId,
            Long quantity, // FIXED: Use Long
            String reservedBy
    ) {
        return create(lotId, productId, quantity, reservedBy, DEFAULT_EXPIRY);
    }
    
    /**
     * Create stock reservation with custom expiry duration
     */
    public static StockReservation create(
            String lotId,
            String productId,
            Long quantity, // FIXED: Use Long
            String reservedBy,
            Duration expiryDuration
    ) {
        // Validation
        if (lotId == null || lotId.isBlank()) {
            throw new InvalidStockReservationException("Lot ID is required");
        }
        if (productId == null || productId.isBlank()) {
            throw new InvalidStockReservationException("Product ID is required");
        }
        if (quantity == null || quantity <= 0) {
            throw new InvalidStockReservationException("Quantity must be positive");
        }
        if (reservedBy == null || reservedBy.isBlank()) {
            throw new InvalidStockReservationException("Reserved by (session/staff ID) is required");
        }
        if (expiryDuration == null || expiryDuration.isNegative()) {
            throw new InvalidStockReservationException("Expiry duration must be positive");
        }
        
        StockReservation reservation = new StockReservation();
        reservation.reservationId = UUID.randomUUID().toString();
        reservation.lotId = lotId;
        reservation.productId = productId;
        reservation.quantity = quantity;
        reservation.reservedBy = reservedBy;
        reservation.reservedAt = LocalDateTime.now();
        reservation.expiresAt = reservation.reservedAt.plus(expiryDuration);
        reservation.status = ReservationStatus.ACTIVE;
        
        log.info("Created stock reservation: {} for lot: {}, qty: {}, expires: {}", 
                reservation.reservationId, lotId, quantity, reservation.expiresAt);
        
        return reservation;
    }
    
    // =================================================================
    // FACTORY: RECONSTITUTE (Load from DB)
    // =================================================================
    
    public static StockReservation reconstitute(
            String reservationId,
            String lotId,
            String productId,
            Long quantity, // FIXED: Use Long
            String reservedBy,
            LocalDateTime reservedAt,
            LocalDateTime expiresAt,
            ReservationStatus status,
            String orderId,
            LocalDateTime committedAt,
            LocalDateTime releasedAt
    ) {
        StockReservation reservation = new StockReservation();
        reservation.reservationId = reservationId;
        reservation.lotId = lotId;
        reservation.productId = productId;
        reservation.quantity = quantity;
        reservation.reservedBy = reservedBy;
        reservation.reservedAt = reservedAt;
        reservation.expiresAt = expiresAt;
        reservation.status = status;
        reservation.orderId = orderId;
        reservation.committedAt = committedAt;
        reservation.releasedAt = releasedAt;
        
        return reservation;
    }
    
    // =================================================================
    // BUSINESS BEHAVIORS
    // =================================================================
    
    /**
     * Commit reservation when order completes
     */
    public void commit(String orderId) {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new InvalidStockReservationException(
                String.format("Cannot commit reservation %s: status is %s", reservationId, status)
            );
        }
        
        this.status = ReservationStatus.COMMITTED;
        this.orderId = orderId;
        this.committedAt = LocalDateTime.now();
        
        log.info("Committed reservation: {} for order: {}", reservationId, orderId);
    }
    
    /**
     * Release reservation (manual cancellation or timeout)
     */
    public void release(String reason) {
        if (this.status == ReservationStatus.COMMITTED) {
            throw new InvalidStockReservationException(
                String.format("Cannot release committed reservation %s", reservationId)
            );
        }
        
        if (this.status == ReservationStatus.RELEASED || this.status == ReservationStatus.EXPIRED) {
            log.warn("Reservation {} already released/expired", reservationId);
            return;
        }
        
        this.status = ReservationStatus.RELEASED;
        this.releasedAt = LocalDateTime.now();
        
        log.info("Released reservation: {} - Reason: {}", reservationId, reason);
    }
    
    /**
     * Mark as expired (called by background job)
     */
    public void expire() {
        if (this.status == ReservationStatus.COMMITTED) {
            log.warn("Attempting to expire committed reservation {}", reservationId);
            return;
        }
        
        if (this.status == ReservationStatus.ACTIVE) {
            this.status = ReservationStatus.EXPIRED;
            this.releasedAt = LocalDateTime.now();
            log.info("Expired reservation: {}", reservationId);
        }
    }
    
    /**
     * Check if reservation has expired
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt) && status == ReservationStatus.ACTIVE;
    }
    
    /**
     * Check if reservation is still active and not expired
     */
    public boolean isActiveAndValid() {
        return status == ReservationStatus.ACTIVE && !isExpired();
    }
    
    /**
     * Extend reservation expiry (e.g., if customer is still at checkout)
     */
    public void extend(Duration additionalTime) {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new InvalidStockReservationException(
                String.format("Cannot extend non-active reservation %s", reservationId)
            );
        }
        
        this.expiresAt = this.expiresAt.plus(additionalTime);
        log.info("Extended reservation: {} until {}", reservationId, expiresAt);
    }
    
    // =================================================================
    // RESERVATION STATUS ENUM
    // =================================================================
    
    public enum ReservationStatus {
        ACTIVE,      // Reservation active, stock reserved
        COMMITTED,   // Order completed, stock deducted
        RELEASED,    // Manually released, stock returned
        EXPIRED      // Auto-expired by timeout, stock returned
    }
}

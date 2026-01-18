package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.StockReservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Port for stock reservation persistence operations
 */
public interface IStockReservationRepository {
    
    /**
     * Save a reservation
     */
    StockReservation save(StockReservation reservation);
    
    /**
     * Find reservation by ID
     */
    Optional<StockReservation> findById(String reservationId);
    
    /**
     * Find all active reservations for a lot
     */
    List<StockReservation> findActiveByLotId(String lotId);
    
    /**
     * Find all expired reservations
     */
    List<StockReservation> findExpired(LocalDateTime now);
    
    /**
     * Find all active reservations by who reserved them
     */
    List<StockReservation> findActiveByReservedBy(String reservedBy);
    
    /**
     * Calculate total reserved quantity for a lot
     */
    long sumReservedQuantityByLotId(String lotId);
}

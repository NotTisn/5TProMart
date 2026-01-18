package com.fivetpromart.infrastructure.persistence.stock_reservation.repository;

import com.fivetpromart.domain.model.StockReservation;
import com.fivetpromart.infrastructure.persistence.stock_reservation.StockReservationDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IStockReservationJpaRepository extends JpaRepository<StockReservationDbo, String> {
    
    /**
     * Find all active reservations for a lot
     */
    @Query("SELECT r FROM StockReservationDbo r WHERE r.lotId = :lotId AND r.status = 'ACTIVE'")
    List<StockReservationDbo> findActiveLotId(@Param("lotId") String lotId);
    
    /**
     * Find all expired reservations that need auto-release
     */
    @Query("SELECT r FROM StockReservationDbo r WHERE r.status = 'ACTIVE' AND r.expiresAt < :now")
    List<StockReservationDbo> findExpired(@Param("now") LocalDateTime now);
    
    /**
     * Find all reservations by session/staff ID (for cleanup)
     */
    @Query("SELECT r FROM StockReservationDbo r WHERE r.reservedBy = :reservedBy AND r.status = 'ACTIVE'")
    List<StockReservationDbo> findActiveByReservedBy(@Param("reservedBy") String reservedBy);
    
    /**
     * Calculate total reserved quantity for a lot
     */
    @Query("SELECT COALESCE(SUM(r.quantity), 0) FROM StockReservationDbo r WHERE r.lotId = :lotId AND r.status = 'ACTIVE'")
    Long sumReservedQuantityByLotId(@Param("lotId") String lotId);
}

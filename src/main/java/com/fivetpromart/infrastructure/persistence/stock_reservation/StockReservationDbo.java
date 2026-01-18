package com.fivetpromart.infrastructure.persistence.stock_reservation;

import com.fivetpromart.domain.model.StockReservation;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_reservations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StockReservationDbo {
    
    @Id
    @Column(name = "reservation_id", length = 36)
    String reservationId;
    
    @Column(name = "lot_id", length = 36, nullable = false)
    String lotId;
    
    @Column(name = "product_id", length = 36, nullable = false)
    String productId;
    
    @Column(name = "quantity", nullable = false)
    Long quantity; // FIXED: Use Long to match domain
    
    @Column(name = "reserved_by", length = 255, nullable = false)
    String reservedBy;
    
    @Column(name = "reserved_at", nullable = false)
    LocalDateTime reservedAt;
    
    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    StockReservation.ReservationStatus status;
    
    @Column(name = "order_id", length = 36)
    String orderId;
    
    @Column(name = "committed_at")
    LocalDateTime committedAt;
    
    @Column(name = "released_at")
    LocalDateTime releasedAt;
}

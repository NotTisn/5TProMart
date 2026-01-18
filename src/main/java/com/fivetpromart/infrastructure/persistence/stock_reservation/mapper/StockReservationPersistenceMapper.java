package com.fivetpromart.infrastructure.persistence.stock_reservation.mapper;

import com.fivetpromart.domain.model.StockReservation;
import com.fivetpromart.infrastructure.persistence.stock_reservation.StockReservationDbo;
import org.springframework.stereotype.Component;

@Component
public class StockReservationPersistenceMapper {
    
    public StockReservationDbo toDbo(StockReservation domain) {
        if (domain == null) return null;
        
        return StockReservationDbo.builder()
                .reservationId(domain.getReservationId())
                .lotId(domain.getLotId())
                .productId(domain.getProductId())
                .quantity(domain.getQuantity())
                .reservedBy(domain.getReservedBy())
                .reservedAt(domain.getReservedAt())
                .expiresAt(domain.getExpiresAt())
                .status(domain.getStatus())
                .orderId(domain.getOrderId())
                .committedAt(domain.getCommittedAt())
                .releasedAt(domain.getReleasedAt())
                .build();
    }
    
    public StockReservation toDomain(StockReservationDbo dbo) {
        if (dbo == null) return null;
        
        return StockReservation.reconstitute(
                dbo.getReservationId(),
                dbo.getLotId(),
                dbo.getProductId(),
                dbo.getQuantity(),
                dbo.getReservedBy(),
                dbo.getReservedAt(),
                dbo.getExpiresAt(),
                dbo.getStatus(),
                dbo.getOrderId(),
                dbo.getCommittedAt(),
                dbo.getReleasedAt()
        );
    }
}

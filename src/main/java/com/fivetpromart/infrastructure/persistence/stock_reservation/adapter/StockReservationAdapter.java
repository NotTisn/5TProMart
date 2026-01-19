package com.fivetpromart.infrastructure.persistence.stock_reservation.adapter;

import com.fivetpromart.application.port.out.IStockReservationRepository;
import com.fivetpromart.domain.model.StockReservation;
import com.fivetpromart.infrastructure.persistence.stock_reservation.mapper.StockReservationPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.stock_reservation.repository.IStockReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockReservationAdapter implements IStockReservationRepository {
    
    private final IStockReservationJpaRepository jpaRepository;
    private final StockReservationPersistenceMapper mapper;
    
    @Override
    public StockReservation save(StockReservation reservation) {
        var dbo = mapper.toDbo(reservation);
        var saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }
    
    @Override
    public Optional<StockReservation> findById(String reservationId) {
        return jpaRepository.findById(reservationId)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<StockReservation> findActiveByLotId(String lotId) {
        return jpaRepository.findActiveLotId(lotId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<StockReservation> findExpired(LocalDateTime now) {
        return jpaRepository.findExpired(now).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<StockReservation> findActiveByReservedBy(String reservedBy) {
        return jpaRepository.findActiveByReservedBy(reservedBy).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public long sumReservedQuantityByLotId(String lotId) {
        return jpaRepository.sumReservedQuantityByLotId(lotId);
    }
}

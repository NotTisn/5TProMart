package com.fivetpromart.infrastructure.persistence.stock_inventory.adapter;

import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import com.fivetpromart.infrastructure.persistence.stock_inventory.mapper.StockInventoryPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.stock_inventory.repository.IStockInventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StockInventoryAdapter implements IStockInventoryRepository {

    private final IStockInventoryJpaRepository jpaRepository;
    private final StockInventoryPersistenceMapper mapper;

    @Override
    public boolean existsById(String lotId) {
        return false;
    }

    @Override
    public void deleteById(String lotId) {

    }

    @Override
    public StockInventory save(StockInventory model) {
        StockInventoryDbo dbo = mapper.toDbo(model);
        StockInventoryDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<StockInventory> findById(String lotId) {
        return Optional.empty();
    }
}

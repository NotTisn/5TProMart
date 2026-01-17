package com.fivetpromart.infrastructure.persistence.stock_inventory.adapter;

import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.infrastructure.persistence.stock_inventory.spec.StockInventorySpecification;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import com.fivetpromart.infrastructure.persistence.stock_inventory.mapper.StockInventoryPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.stock_inventory.repository.IStockInventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StockInventoryAdapter implements IStockInventoryRepository {

    private final IStockInventoryJpaRepository jpaRepository;
    private final StockInventoryPersistenceMapper mapper;

    @Override
    public boolean existsById(String lotId) {
        return jpaRepository.existsById(lotId);
    }

    @Override
    public void deleteById(String lotId) {
        jpaRepository.deleteById(lotId);
    }

    @Override
    public StockInventory save(StockInventory model) {
        StockInventoryDbo dbo = mapper.toDbo(model);
        StockInventoryDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<StockInventory> findById(String lotId) {
        return jpaRepository.findById(lotId)
                .map(mapper::toDomain);
    }

    @Override
    public List<StockInventory> searchStockInventories(StockInventorySearchQuery query) {
        Specification<StockInventoryDbo> spec = StockInventorySpecification.getSpecification(query);
        Sort sort = buildSort(query);

        List<StockInventoryDbo> dbos = jpaRepository.findAll(spec, sort);

        return dbos.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<StockInventory> searchStockInventories(StockInventorySearchQuery query, Pageable pageable) {
        Specification<StockInventoryDbo> spec = StockInventorySpecification.getSpecification(query);
        Page<StockInventoryDbo> dboPage = jpaRepository.findAll(spec, pageable);

        return dboPage.map(mapper::toDomain);
    }

    /**
     * Build sort from query parameters
     */
    private Sort buildSort(StockInventorySearchQuery query) {
        String sortBy = query.getSortBy() != null ? query.getSortBy() : "expirationDate";
        String order = query.getOrder() != null ? query.getOrder() : "asc";

        Sort.Direction direction = "desc".equalsIgnoreCase(order)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return Sort.by(direction, sortBy);
    }

    @Override
    public Long getTotalStockByProductId(String productId) {
        return jpaRepository.sumStockQuantityByProductId(productId);
    }

    @Override
    public Long countByStockQuantityLessThan(Long threshold) {
        return jpaRepository.countByStockQuantityLessThan(threshold);
    }

    @Override
    public Long countByStockQuantityEquals(Long quantity) {
        return jpaRepository.countByStockQuantity(quantity);
    }

    @Override
    public Long countByExpirationDateBefore(LocalDate date) {
        return jpaRepository.countByExpirationDateBefore(date);
    }

    @Override
    public Long countByExpirationDateBetween(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.countByExpirationDateBetween(startDate, endDate);
    }

    @Override
    public BigDecimal calculateTotalInventoryValue() {
        return jpaRepository.calculateTotalInventoryValue();
    }
}

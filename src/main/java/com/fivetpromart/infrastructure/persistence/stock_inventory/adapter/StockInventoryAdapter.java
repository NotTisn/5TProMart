package com.fivetpromart.infrastructure.persistence.stock_inventory.adapter;

import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.enums.BatchStatus;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.infrastructure.persistence.stock_inventory.spec.StockInventorySpecification;
import com.fivetpromart.infrastructure.persistence.stock_inventory.StockInventoryDbo;
import com.fivetpromart.infrastructure.persistence.stock_inventory.mapper.StockInventoryPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.stock_inventory.repository.IStockInventoryJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean existsById(String lotId) {
        return jpaRepository.existsById(lotId);
    }

    @Override
    public void deleteById(String lotId) {
        jpaRepository.deleteById(lotId);
    }

    /**
     * Saves a StockInventory domain model to the database.
     * 
     * This method handles the case where an entity may already exist in the Hibernate session
     * (e.g., after findById or findByIdForUpdate). To prevent NonUniqueObjectException and
     * StaleObjectStateException with @Version fields, we:
     * 
     * 1. For EXISTING entities: Load the managed entity and copy fields to it
     * 2. For NEW entities: Create and persist a new DBO
     * 
     * This approach:
     * - Respects optimistic locking (@Version)
     * - Preserves audit fields (createdAt, updatedBy, etc.)
     * - Avoids session conflicts with detached entities
     * - Keeps the domain model free from infrastructure concerns
     */
    @Override
    public StockInventory save(StockInventory model) {
        String lotId = model.getLotId();
        
        // Check if entity already exists (either in session or DB)
        StockInventoryDbo existingDbo = jpaRepository.findById(lotId).orElse(null);
        
        if (existingDbo != null) {
            // UPDATE: Copy fields from domain model to the managed entity
            // This preserves version, audit fields, and session state
            existingDbo.setProductId(model.getProductId());
            existingDbo.setManufactureDate(model.getManufactureDate());
            existingDbo.setExpirationDate(model.getExpirationDate());
            existingDbo.setStockQuantity(model.getStockQuantity());
            existingDbo.setReservedQuantity(model.getReservedQuantity());
            existingDbo.setImportPrice(model.getImportPrice());
            // Convert enum to string for persistence
            existingDbo.setStatus(model.getStatus() != null ? model.getStatus().getValue() : BatchStatus.AVAILABLE.getValue());
            // No need to call save() - entity is managed, changes are auto-flushed
            return mapper.toDomain(existingDbo);
        } else {
            // CREATE: New entity - create fresh DBO
            StockInventoryDbo newDbo = mapper.toDbo(model);
            StockInventoryDbo savedDbo = jpaRepository.save(newDbo);
            return mapper.toDomain(savedDbo);
        }
    }

    @Override
    public Optional<StockInventory> findById(String lotId) {
        return jpaRepository.findById(lotId)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<StockInventory> findByIdForUpdate(String lotId) {
        return jpaRepository.findByIdForUpdate(lotId)
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

    @Override
    public List<StockInventory> findExpiredButNotMarked(LocalDate today) {
        // Find lots where expirationDate < today AND status is still AVAILABLE
        List<StockInventoryDbo> dbos = jpaRepository.findByExpirationDateBeforeAndStatus(
                today, 
                BatchStatus.AVAILABLE.getValue()
        );
        return dbos.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll(List<StockInventory> inventories) {
        for (StockInventory inventory : inventories) {
            save(inventory);
        }
    }
}

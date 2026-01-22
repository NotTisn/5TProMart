package com.fivetpromart.infrastructure.persistence.purchase_order.adapter;

import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import com.fivetpromart.application.port.out.IPurchaseOrderRepository;
import com.fivetpromart.domain.model.PurchaseOrder;
import com.fivetpromart.infrastructure.persistence.purchase_order.PurchaseOrderDbo;
import com.fivetpromart.infrastructure.persistence.purchase_order.mapper.PurchaseOrderPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.purchase_order.repository.IPurchaseOrderJpaRepository;
import com.fivetpromart.infrastructure.persistence.purchase_order.spec.PurchaseOrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PurchaseOrderAdapter implements IPurchaseOrderRepository {

    private final IPurchaseOrderJpaRepository jpaRepository;
    private final PurchaseOrderPersistenceMapper mapper;

    @Override
    public PurchaseOrder save(PurchaseOrder purchaseOrder) {
        PurchaseOrderDbo dbo = mapper.toDbo(purchaseOrder);
        PurchaseOrderDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<PurchaseOrder> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<PurchaseOrder> searchPurchaseOrders(PurchaseOrderSearchQuery query, Pageable pageable) {
        // Build specification
        Specification<PurchaseOrderDbo> spec = PurchaseOrderSpecification.getSpecification(query);

        // Build sort
        Sort sort = buildSort(query);
        Pageable pageableWithSort = org.springframework.data.domain.PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        // Execute query
        Page<PurchaseOrderDbo> dboPage = jpaRepository.findAll(spec, pageableWithSort);

        // Map to domain
        return dboPage.map(mapper::toDomain);
    }

    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }

    private Sort buildSort(PurchaseOrderSearchQuery query) {
        String sortBy = query.getSortBy() != null ? query.getSortBy() : "purchaseDate";
        String order = query.getOrder() != null ? query.getOrder() : "desc";

        Sort.Direction direction = "asc".equalsIgnoreCase(order)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return Sort.by(direction, sortBy);
    }
}

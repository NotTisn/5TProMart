package com.fivetpromart.infrastructure.persistence.purchase_order.repository;

import com.fivetpromart.infrastructure.persistence.purchase_order.PurchaseOrderDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IPurchaseOrderJpaRepository extends
        JpaRepository<PurchaseOrderDbo, String>,
        JpaSpecificationExecutor<PurchaseOrderDbo> {
    boolean existsById(String id);
}

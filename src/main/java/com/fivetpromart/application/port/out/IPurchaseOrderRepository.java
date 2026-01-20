package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import com.fivetpromart.domain.model.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPurchaseOrderRepository {
    PurchaseOrder save(PurchaseOrder purchaseOrder);
    Optional<PurchaseOrder> findById(String id);
    Page<PurchaseOrder> searchPurchaseOrders(PurchaseOrderSearchQuery query, Pageable pageable);
    boolean existsById(String id);
}

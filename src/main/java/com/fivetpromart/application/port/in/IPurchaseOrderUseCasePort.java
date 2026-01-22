package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.PurchaseOrderDto;
import com.fivetpromart.application.dto.command.PurchaseOrderCancelCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderConfirmCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderCreationCommand;
import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPurchaseOrderUseCasePort {
    
    /**
     * Get all purchase orders with filters and pagination
     */
    Page<PurchaseOrderDto> searchPurchaseOrders(PurchaseOrderSearchQuery query, Pageable pageable);
    
    /**
     * Get purchase order detail by ID
     */
    PurchaseOrderDto getPurchaseOrderById(String id);
    
    /**
     * Create a draft purchase order
     */
    PurchaseOrderDto createDraftPurchaseOrder(PurchaseOrderCreationCommand command);
    
    /**
     * Confirm purchase order and generate lots
     */
    PurchaseOrderDto confirmPurchaseOrder(String id, PurchaseOrderConfirmCommand command);
    
    /**
     * Cancel purchase order
     */
    PurchaseOrderDto cancelPurchaseOrder(String id, PurchaseOrderCancelCommand command);
    
    /**
     * Get labels for reprinting
     */
    List<PurchaseOrderDto.LotToPrintDto> getLabelsForReprint(String id);
}

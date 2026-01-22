package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.PurchaseOrderDto;
import com.fivetpromart.application.dto.command.PurchaseOrderCancelCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderConfirmCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderCreationCommand;
import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import com.fivetpromart.application.mapper.PurchaseOrderDataMapper;
import com.fivetpromart.application.port.in.IPurchaseOrderUseCasePort;
import com.fivetpromart.application.port.out.IPurchaseOrderRepository;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.application.port.out.ISupplierRepository;
import com.fivetpromart.domain.exception.InvalidPurchaseOrderException;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.PurchaseOrder;
import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.domain.model.StockInventory;
import com.fivetpromart.domain.model.Supplier;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseOrderUseCase implements IPurchaseOrderUseCasePort {

    private final IPurchaseOrderRepository purchaseOrderRepository;
    private final ISupplierRepository supplierRepository;
    private final IProductRepository productRepository;
    private final IStockInventoryRepository stockInventoryRepository;
    private final IStaffRepository staffRepository;
    private final PurchaseOrderDataMapper mapper;
    private final ProductUseCase productUseCase;

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseOrderDto> searchPurchaseOrders(PurchaseOrderSearchQuery query, Pageable pageable) {
        log.info("Searching purchase orders with query: {}", query);
        Page<PurchaseOrder> purchaseOrders = purchaseOrderRepository.searchPurchaseOrders(query, pageable);
        return purchaseOrders.map(po -> enrichWithStaffName(mapper.toDto(po)));
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderDto getPurchaseOrderById(String id) {
        log.info("Getting purchase order by ID: {}", id);
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found with ID: " + id));
        return enrichWithStaffName(mapper.toDto(purchaseOrder));
    }

    @Override
    public PurchaseOrderDto createDraftPurchaseOrder(PurchaseOrderCreationCommand command) {
        log.info("Creating draft purchase order for supplier: {}", command.getSupplierId());

        // Validate supplier exists
        Supplier supplier = supplierRepository.findById(command.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with ID: " + command.getSupplierId()));

        // Create items with product names
        List<PurchaseOrder.PurchaseOrderItem> items = new ArrayList<>();
        for (PurchaseOrderCreationCommand.PurchaseOrderItemCommand itemCommand : command.getItems()) {
            Product product = productRepository.findById(itemCommand.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemCommand.getProductId()));

            PurchaseOrder.PurchaseOrderItem item = PurchaseOrder.PurchaseOrderItem.createDraft(
                    product.getProductId(),
                    product.getProductName(),
                    itemCommand.getQuantityOrdered()
            );
            items.add(item);
        }

        // Get current staff ID (from security context or parameter)
        String staffIdCreated = "current-staff-id"; // TODO: Get from SecurityContext

        // Create draft purchase order
        PurchaseOrder purchaseOrder = PurchaseOrder.createDraft(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getPhoneNumber(),
                supplier.getRepresentName(),
                supplier.getRepresentPhoneNumber(),
                staffIdCreated,
                command.getNotes(),
                items
        );

        // Save
        PurchaseOrder savedPO = purchaseOrderRepository.save(purchaseOrder);
        log.info("Draft purchase order created: {}", savedPO.getPoCode());

        return mapper.toDto(savedPO);
    }

    @Override
    public PurchaseOrderDto confirmPurchaseOrder(String id, PurchaseOrderConfirmCommand command) {
        log.info("Confirming purchase order: {}", id);

        // Find purchase order
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found with ID: " + id));

        // Create invoice
        PurchaseOrder.Invoice invoice = null;
        if (command.getInvoice() != null) {
            invoice = PurchaseOrder.Invoice.create(
                    command.getInvoice().getInvoiceNumber(),
                    command.getInvoice().getInvoiceDate(),
                    command.getInvoice().getImages()
            );
        }

        // Create actual items with received quantities
        List<PurchaseOrder.PurchaseOrderItem> actualItems = new ArrayList<>();
        for (PurchaseOrderConfirmCommand.ActualItemCommand actualItemCmd : command.getActualItems()) {
            Product product = productRepository.findById(actualItemCmd.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + actualItemCmd.getProductId()));

            PurchaseOrder.PurchaseOrderItem item = PurchaseOrder.PurchaseOrderItem.reconstitute(
                    null,
                    null,
                    product.getProductId(),
                    product.getProductName(),
                    actualItemCmd.getImportPrice(),
                    null,
                    actualItemCmd.getQuantityReceived(),
                    null
            );
            actualItems.add(item);
        }

        // Confirm order
        purchaseOrder.confirm(
                command.getStaffIdChecked(),
                command.getCheckDate(),
                command.getNotes(),
                invoice,
                actualItems
        );

        // Process lot merging and update stock
        List<PurchaseOrder.LotToPrint> lotsToPrint = new ArrayList<>();
        
        for (PurchaseOrderConfirmCommand.ActualItemCommand actualItemCmd : command.getActualItems()) {
            // Find or create lot
            String lotId = findOrCreateLot(
                    actualItemCmd.getProductId(),
                    actualItemCmd.getManufactureDate(),
                    actualItemCmd.getExpirationDate(),
                    actualItemCmd.getQuantityReceived(),
                    actualItemCmd.getImportPrice()
            );

            // Add to generated lots
            purchaseOrder.addGeneratedLot(lotId);

            // Create lot to print
            Product product = productRepository.findById(actualItemCmd.getProductId()).get();
            PurchaseOrder.LotToPrint lotToPrint = PurchaseOrder.LotToPrint.create(
                    lotId,
                    product.getProductName(),
                    actualItemCmd.getQuantityReceived(),
                    actualItemCmd.getExpirationDate(),
                    actualItemCmd.getNotes()
            );
            lotsToPrint.add(lotToPrint);

            // Update product total stock quantity
            productUseCase.updateTotalStockQuantity(actualItemCmd.getProductId());
        }

        // Save purchase order
        PurchaseOrder savedPO = purchaseOrderRepository.save(purchaseOrder);
        log.info("Purchase order confirmed: {}", savedPO.getPoCode());

        // Return DTO with lots to print
        PurchaseOrderDto dto = mapper.toDto(savedPO);
        return dto;
    }

    @Override
    public PurchaseOrderDto cancelPurchaseOrder(String id, PurchaseOrderCancelCommand command) {
        log.info("Cancelling purchase order: {}", id);

        // Find purchase order
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found with ID: " + id));

        // Cancel order
        purchaseOrder.cancel(
                command.getStaffIdChecked(),
                command.getCheckDate(),
                command.getCancelNotesReason()
        );

        // Save
        PurchaseOrder savedPO = purchaseOrderRepository.save(purchaseOrder);
        log.info("Purchase order cancelled: {}", savedPO.getPoCode());

        return mapper.toDto(savedPO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderDto.LotToPrintDto> getLabelsForReprint(String id) {
        log.info("Getting labels for reprint: {}", id);

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase order not found with ID: " + id));

        if (!"Completed".equals(purchaseOrder.getStatus())) {
            throw new InvalidPurchaseOrderException("Only completed orders can have labels reprinted");
        }

        // Get lots from generated lot IDs
        List<PurchaseOrder.LotToPrint> lotsToPrint = new ArrayList<>();
        for (String lotId : purchaseOrder.getGeneratedLotIds()) {
            StockInventory lot = stockInventoryRepository.findById(lotId)
                    .orElseThrow(() -> new EntityNotFoundException("Lot not found: " + lotId));

            Product product = productRepository.findById(lot.getProductId()).get();

            PurchaseOrder.LotToPrint lotToPrint = PurchaseOrder.LotToPrint.create(
                    lot.getLotId(),
                    product.getProductName(),
                    lot.getStockQuantity(),
                    lot.getExpirationDate(),
                    ""
            );
            lotsToPrint.add(lotToPrint);
        }

        return lotsToPrint.stream()
                .map(mapper::toLotToPrintDto)
                .collect(Collectors.toList());
    }

    // =================================================================
    // HELPER: FIND OR CREATE LOT (LOT MERGING LOGIC)
    // =================================================================
    private String findOrCreateLot(
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long quantityReceived,
            java.math.BigDecimal importPrice
    ) {
        // Search for existing lot with same productId and expirationDate
        List<StockInventory> existingLots = stockInventoryRepository.searchStockInventories(
                com.fivetpromart.application.dto.query.StockInventorySearchQuery.builder()
                        .productId(productId)
                        .build()
        );

        // Find matching lot (handle null expirationDate for non-perishable items)
        Optional<StockInventory> matchingLot = existingLots.stream()
                .filter(lot -> java.util.Objects.equals(lot.getExpirationDate(), expirationDate))
                .findFirst();

        if (matchingLot.isPresent()) {
            // Merge: Add quantity to existing lot
            StockInventory lot = matchingLot.get();
            lot.update(
                    null,
                    null,
                    null,
                    lot.getStockQuantity() + quantityReceived,
                    null,
                    lot.getStatus()
            );
            StockInventory savedLot = stockInventoryRepository.save(lot);
            log.info("Merged into existing lot: {}", savedLot.getLotId());
            return savedLot.getLotId();
        } else {
            // Create new lot
            StockInventory newLot = StockInventory.create(
                    productId,
                    manufactureDate,
                    expirationDate,
                    quantityReceived,
                    importPrice
            );
            StockInventory savedLot = stockInventoryRepository.save(newLot);
            log.info("Created new lot: {}", savedLot.getLotId());
            return savedLot.getLotId();
        }
    }

    // Helper method to enrich DTO with staff name
    private PurchaseOrderDto enrichWithStaffName(PurchaseOrderDto dto) {
        if (dto.getStaffIdCreated() != null && !dto.getStaffIdCreated().isBlank()) {
            try {
                return staffRepository.findById(dto.getStaffIdCreated())
                        .map(staff -> dto.toBuilder()
                                .staffNameCreated(staff.getFullName())
                                .build())
                        .orElse(dto);
            } catch (Exception e) {
                log.warn("Could not fetch staff name for staffId: {}", dto.getStaffIdCreated());
                return dto;
            }
        }
        return dto;
    }
}

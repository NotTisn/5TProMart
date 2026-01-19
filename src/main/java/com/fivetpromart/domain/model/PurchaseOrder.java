package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InvalidPurchaseOrderException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrder {
    private String id;
    private String poCode;
    private SupplierInfo supplier;
    private String staffIdCreated;
    private String staffIdChecked;
    private String status; // Draft, Completed, Cancelled
    private String notes;
    private String cancellationReason;
    private BigDecimal totalAmount;
    private LocalDate purchaseDate;
    private LocalDate checkDate;
    private Invoice invoice;
    private List<PurchaseOrderItem> items;
    private List<String> generatedLotIds;

    // =================================================================
    // FACTORY: CREATE DRAFT
    // =================================================================
    public static PurchaseOrder createDraft(
            String supplierId,
            String supplierName,
            String phone,
            String representName,
            String representPhoneNumber,
            String staffIdCreated,
            String notes,
            List<PurchaseOrderItem> items
    ) {
        if (supplierId == null || supplierId.isBlank())
            throw new EmptyFieldException("Supplier ID");
        if (items == null || items.isEmpty())
            throw new EmptyFieldException("Items");

        PurchaseOrder po = new PurchaseOrder();
        po.id = UUID.randomUUID().toString();
        po.poCode = "PO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        po.supplier = SupplierInfo.create(supplierId, supplierName, phone, representName, representPhoneNumber);
        po.staffIdCreated = staffIdCreated;
        po.status = "Draft";
        po.notes = notes;
        po.purchaseDate = LocalDate.now();
        po.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        po.generatedLotIds = new ArrayList<>();
        
        // Set orderId for each item
        for (PurchaseOrderItem item : po.items) {
            item.setPurchaseOrderId(po.id);
        }
        
        // Calculate total
        po.calculateTotal();
        
        return po;
    }

    // =================================================================
    // FACTORY: RECONSTITUTE
    // =================================================================
    public static PurchaseOrder reconstitute(
            String id,
            String poCode,
            SupplierInfo supplier,
            String staffIdCreated,
            String staffIdChecked,
            String status,
            String notes,
            String cancellationReason,
            BigDecimal totalAmount,
            LocalDate purchaseDate,
            LocalDate checkDate,
            Invoice invoice,
            List<PurchaseOrderItem> items,
            List<String> generatedLotIds
    ) {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.poCode = poCode;
        po.supplier = supplier;
        po.staffIdCreated = staffIdCreated;
        po.staffIdChecked = staffIdChecked;
        po.status = status;
        po.notes = notes;
        po.cancellationReason = cancellationReason;
        po.totalAmount = totalAmount;
        po.purchaseDate = purchaseDate;
        po.checkDate = checkDate;
        po.invoice = invoice;
        po.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        po.generatedLotIds = generatedLotIds != null ? new ArrayList<>(generatedLotIds) : new ArrayList<>();
        return po;
    }

    // =================================================================
    // BUSINESS LOGIC: CONFIRM ORDER
    // =================================================================
    public void confirm(
            String staffIdChecked,
            LocalDate checkDate,
            String notes,
            Invoice invoice,
            List<PurchaseOrderItem> actualItems
    ) {
        if (!"Draft".equals(this.status)) {
            throw new InvalidPurchaseOrderException("Only Draft orders can be confirmed. Current status: " + this.status);
        }
        if (actualItems == null || actualItems.isEmpty()) {
            throw new EmptyFieldException("Actual items");
        }

        this.staffIdChecked = staffIdChecked;
        this.checkDate = checkDate;
        this.notes = notes;
        this.invoice = invoice;
        this.status = "Completed";
        
        // Update items with actual received data
        for (PurchaseOrderItem actualItem : actualItems) {
            // Find matching item in original order
            this.items.stream()
                    .filter(item -> item.getProductId().equals(actualItem.getProductId()))
                    .findFirst()
                    .ifPresent(item -> {
                        item.updateReceived(
                                actualItem.getQuantityReceived(),
                                actualItem.getImportPrice()
                        );
                    });
        }
        
        // Recalculate total based on actual received
        this.calculateTotal();
    }

    // =================================================================
    // BUSINESS LOGIC: CANCEL ORDER
    // =================================================================
    public void cancel(String staffIdChecked, LocalDate checkDate, String cancelReason) {
        if (!"Draft".equals(this.status)) {
            throw new InvalidPurchaseOrderException("Only Draft orders can be cancelled. Current status: " + this.status);
        }

        this.staffIdChecked = staffIdChecked;
        this.checkDate = checkDate;
        this.cancellationReason = cancelReason;
        this.status = "Cancelled";
    }

    // =================================================================
    // BUSINESS LOGIC: ADD GENERATED LOT
    // =================================================================
    public void addGeneratedLot(String lotId) {
        if (this.generatedLotIds == null) {
            this.generatedLotIds = new ArrayList<>();
        }
        this.generatedLotIds.add(lotId);
    }

    // =================================================================
    // HELPER: CALCULATE TOTAL
    // =================================================================
    private void calculateTotal() {
        this.totalAmount = this.items.stream()
                .map(PurchaseOrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =================================================================
    // NESTED CLASS: PURCHASE ORDER ITEM
    // =================================================================
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PurchaseOrderItem {
        private String itemId;
        private String purchaseOrderId;
        private String productId;
        private String productName;
        private BigDecimal importPrice;
        private Long quantityOrdered;
        private Long quantityReceived;
        private BigDecimal subTotal;

        public static PurchaseOrderItem createDraft(
                String productId,
                String productName,
                Long quantityOrdered
        ) {
            if (productId == null || productId.isBlank())
                throw new EmptyFieldException("Product ID");
            if (quantityOrdered == null || quantityOrdered <= 0)
                throw new InvalidPurchaseOrderException("Quantity ordered must be greater than 0");

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.itemId = UUID.randomUUID().toString();
            item.productId = productId;
            item.productName = productName;
            item.quantityOrdered = quantityOrdered;
            item.quantityReceived = 0L;
            item.importPrice = BigDecimal.ZERO;
            item.subTotal = BigDecimal.ZERO;
            return item;
        }

        public static PurchaseOrderItem reconstitute(
                String itemId,
                String purchaseOrderId,
                String productId,
                String productName,
                BigDecimal importPrice,
                Long quantityOrdered,
                Long quantityReceived,
                BigDecimal subTotal
        ) {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.itemId = itemId;
            item.purchaseOrderId = purchaseOrderId;
            item.productId = productId;
            item.productName = productName;
            item.importPrice = importPrice;
            item.quantityOrdered = quantityOrdered;
            item.quantityReceived = quantityReceived;
            item.subTotal = subTotal;
            return item;
        }

        public void setPurchaseOrderId(String purchaseOrderId) {
            this.purchaseOrderId = purchaseOrderId;
        }

        public void updateReceived(Long quantityReceived, BigDecimal importPrice) {
            this.quantityReceived = quantityReceived;
            this.importPrice = importPrice;
            this.subTotal = importPrice.multiply(BigDecimal.valueOf(quantityReceived));
        }
    }

    // =================================================================
    // NESTED CLASS: SUPPLIER INFO (Value Object)
    // =================================================================
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SupplierInfo {
        private String supplierId;
        private String supplierName;
        private String phone;
        private String representName;
        private String representPhoneNumber;

        public static SupplierInfo create(
                String supplierId,
                String supplierName,
                String phone,
                String representName,
                String representPhoneNumber
        ) {
            SupplierInfo info = new SupplierInfo();
            info.supplierId = supplierId;
            info.supplierName = supplierName;
            info.phone = phone;
            info.representName = representName;
            info.representPhoneNumber = representPhoneNumber;
            return info;
        }

        public static SupplierInfo reconstitute(
                String supplierId,
                String supplierName,
                String phone,
                String representName,
                String representPhoneNumber
        ) {
            return create(supplierId, supplierName, phone, representName, representPhoneNumber);
        }
    }

    // =================================================================
    // NESTED CLASS: INVOICE (Value Object)
    // =================================================================
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Invoice {
        private String invoiceNumber;
        private LocalDate invoiceDate;
        private List<String> images;

        public static Invoice create(
                String invoiceNumber,
                LocalDate invoiceDate,
                List<String> images
        ) {
            Invoice invoice = new Invoice();
            invoice.invoiceNumber = invoiceNumber;
            invoice.invoiceDate = invoiceDate;
            invoice.images = images != null ? new ArrayList<>(images) : new ArrayList<>();
            return invoice;
        }

        public static Invoice reconstitute(
                String invoiceNumber,
                LocalDate invoiceDate,
                List<String> images
        ) {
            return create(invoiceNumber, invoiceDate, images);
        }
    }

    // =================================================================
    // NESTED CLASS: LOT TO PRINT (Value Object)
    // =================================================================
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class LotToPrint {
        private String lotId;
        private String productName;
        private Long quantity;
        private LocalDate expirationDate;
        private String notes;

        public static LotToPrint create(
                String lotId,
                String productName,
                Long quantity,
                LocalDate expirationDate,
                String notes
        ) {
            LotToPrint lot = new LotToPrint();
            lot.lotId = lotId;
            lot.productName = productName;
            lot.quantity = quantity;
            lot.expirationDate = expirationDate;
            lot.notes = notes;
            return lot;
        }
    }
}

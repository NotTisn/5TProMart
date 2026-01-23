package com.fivetpromart.domain.model;

import com.fivetpromart.domain.enums.BatchStatus;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InvalidDateRangeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockInventory {
    private String lotId;
    private String productId;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long stockQuantity;
    private Long reservedQuantity; // Track how much is currently reserved
    private Long quantityShelf;    // Display quantity (items on shelf/display)
    private Long quantityStorage;  // Warehouse quantity (items in storage)
    private BigDecimal importPrice;
    private BatchStatus status;

    public static StockInventory create(String productId, LocalDate manufactureDate, LocalDate expirationDate, Long stockQuantity, BigDecimal importPrice) {
        if(productId == null || productId.isBlank())
            throw new EmptyFieldException("Product ID");
        if(manufactureDate == null)
            throw new EmptyFieldException("Manufacture date");
        if(expirationDate == null)
            throw new EmptyFieldException("Expiration date");
        if(stockQuantity == null)
            throw new EmptyFieldException("Stock quantity");
        if(importPrice == null)
            throw new EmptyFieldException("Import price");
        if(expirationDate.isBefore(manufactureDate))
            throw new InvalidDateRangeException();

        StockInventory stockInventory = new StockInventory();
        stockInventory.lotId = UUID.randomUUID().toString();
        stockInventory.productId = productId;
        stockInventory.manufactureDate = manufactureDate;
        stockInventory.expirationDate = expirationDate;
        stockInventory.stockQuantity = stockQuantity;
        stockInventory.reservedQuantity = 0L;
        stockInventory.quantityShelf = 0L;              // Per spec: starts at 0
        stockInventory.quantityStorage = stockQuantity;  // Per spec: starts at stockQuantity
        stockInventory.importPrice = importPrice;
        stockInventory.status = BatchStatus.AVAILABLE;

        return stockInventory;
    }

    public static StockInventory reconstitute(
            String lotId,
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long stockQuantity,
            Long reservedQuantity,
            Long quantityShelf,
            Long quantityStorage,
            BigDecimal importPrice,
            String statusString
    ) {
        StockInventory stockInventory = new StockInventory();
        stockInventory.lotId = lotId;
        stockInventory.productId = productId;
        stockInventory.manufactureDate = manufactureDate;
        stockInventory.expirationDate = expirationDate;
        stockInventory.stockQuantity = stockQuantity;
        stockInventory.reservedQuantity = reservedQuantity != null ? reservedQuantity : 0L;
        stockInventory.quantityShelf = quantityShelf != null ? quantityShelf : 0L;
        stockInventory.quantityStorage = quantityStorage != null ? quantityStorage : stockQuantity;
        stockInventory.importPrice = importPrice;
        stockInventory.status = BatchStatus.fromString(statusString);
        return stockInventory;
    }

    public void update(
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long stockQuantity,
            BigDecimal importPrice,
            BatchStatus status) {
        if(productId != null && !productId.isBlank())
            this.productId = productId;
        if(dateValidation(manufactureDate, expirationDate)) {
            this.manufactureDate = manufactureDate;
            this.expirationDate = expirationDate;
        }
        if(stockQuantity != null)
            this.stockQuantity = stockQuantity;
        if(importPrice != null)
            this.importPrice = importPrice;
        if(status != null)
            this.status = status;
    }
    
    /**
     * Deduct stock for a direct sale (no reservation)
     * Business rule: Customers buy from shelf first, then storage
     */
    public void deductForSale(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Deduction quantity must be positive");
        }
        if (stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient stock to deduct");
        }
        
        long shelfQty = quantityShelf != null ? quantityShelf : 0L;
        long storageQty = quantityStorage != null ? quantityStorage : 0L;
        
        if (shelfQty >= quantity) {
            // All from shelf
            this.quantityShelf = shelfQty - quantity;
        } else {
            // Take all shelf, remainder from storage
            long fromStorage = quantity - shelfQty;
            this.quantityShelf = 0L;
            this.quantityStorage = storageQty - fromStorage;
        }
        
        this.stockQuantity = stockQuantity - quantity;
    }
    
    /**
     * Restore stock after order cancellation
     * Business rule: Restored stock goes back to shelf (where it was sold from)
     */
    public void restoreForCancellation(Long quantity) {
        if (quantity == null || quantity <= 0) {
            return; // Nothing to restore
        }
        this.stockQuantity += quantity;
        this.quantityShelf = (quantityShelf != null ? quantityShelf : 0L) + quantity;
    }
    
    /**
     * Update shelf and storage quantities
     * Per API Spec: quantityStorage + quantityShelf = availableQuantity (stockQuantity - reservedQuantity)
     * Validation: The sum must equal available stock, and values must be non-negative.
     */
    public void updateShelfStorage(Long newQuantityShelf, Long newQuantityStorage) {
        long available = getAvailableQuantity();
        long shelf = newQuantityShelf != null ? newQuantityShelf : this.quantityShelf;
        long storage = newQuantityStorage != null ? newQuantityStorage : this.quantityStorage;
        
        // Validate non-negative
        if (shelf < 0 || storage < 0) {
            throw new IllegalArgumentException("Shelf and storage quantities must be non-negative");
        }
        
        // Validate sum constraint
        if (shelf + storage != available) {
            throw new IllegalStateException(
                    String.format("Shelf (%d) + Storage (%d) must equal available quantity (%d)", 
                            shelf, storage, available));
        }
        
        this.quantityShelf = shelf;
        this.quantityStorage = storage;
    }
    
    /**
     * Transfer stock from storage to shelf (display)
     * Per API Spec: Khi lấy hàng từ kho ra trưng bày, update quantityShelf += số lượng, trừ quantityStorage
     */
    public void transferToShelf(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Transfer quantity must be positive");
        }
        if (this.quantityStorage < quantity) {
            throw new IllegalStateException("Insufficient storage quantity to transfer");
        }
        this.quantityStorage -= quantity;
        this.quantityShelf += quantity;
    }
    
    /**
     * Transfer stock from shelf back to storage
     */
    public void transferToStorage(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Transfer quantity must be positive");
        }
        if (this.quantityShelf < quantity) {
            throw new IllegalStateException("Insufficient shelf quantity to transfer");
        }
        this.quantityShelf -= quantity;
        this.quantityStorage += quantity;
    }


    /* Helper to validate
    **/
    private boolean dateValidation(
            LocalDate manufactureDate,
            LocalDate expirationDate
            ) {
        if ( manufactureDate == null ||
                expirationDate == null ||
                expirationDate.isBefore(manufactureDate)) {
            return false;
        }

        return true;
    }
    
    /**
     * Get available stock (total stock minus reserved)
     */
    public Long getAvailableQuantity() {
        long reserved = reservedQuantity != null ? reservedQuantity : 0L;
        return stockQuantity - reserved;
    }
    
    /**
     * Reserve stock for a pending order
     */
    public void reserveStock(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Reservation quantity must be positive");
        }
        if (getAvailableQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock to reserve");
        }
        this.reservedQuantity = (reservedQuantity != null ? reservedQuantity : 0L) + quantity;
    }
    
    /**
     * Release reserved stock (when reservation expires or is cancelled)
     */
    public void releaseStock(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Release quantity must be positive");
        }
        long current = reservedQuantity != null ? reservedQuantity : 0L;
        if (current < quantity) {
            throw new IllegalStateException("Cannot release more than reserved");
        }
        this.reservedQuantity = current - quantity;
    }
    
    /**
     * Commit reserved stock (when order is completed)
     * Reduces reserved, total stock, AND shelf quantity (customers buy from shelf)
     * Business rule: When an item is sold, it comes off the shelf, not storage.
     */
    public void commitReservedStock(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Commit quantity must be positive");
        }
        long currentReserved = reservedQuantity != null ? reservedQuantity : 0L;
        if (currentReserved < quantity) {
            throw new IllegalStateException("Cannot commit more than reserved");
        }
        if (stockQuantity < quantity) {
            throw new IllegalStateException("Insufficient total stock to commit");
        }
        
        // Business logic: Customers buy from shelf
        // If shelf has enough, deduct from shelf
        // If shelf is insufficient, deduct what's on shelf, rest from storage
        long shelfQty = quantityShelf != null ? quantityShelf : 0L;
        long storageQty = quantityStorage != null ? quantityStorage : 0L;
        
        if (shelfQty >= quantity) {
            // All from shelf
            this.quantityShelf = shelfQty - quantity;
        } else {
            // Take all shelf, remainder from storage
            long fromStorage = quantity - shelfQty;
            this.quantityShelf = 0L;
            this.quantityStorage = storageQty - fromStorage;
        }
        
        this.reservedQuantity = currentReserved - quantity;
        this.stockQuantity = stockQuantity - quantity;
    }

    /**
     * Update status using string value (for API compatibility)
     */
    public void updateStatus(String statusString) {
        if (statusString != null) {
            this.status = BatchStatus.fromString(statusString);
        }
    }

    /**
     * Get status as string (for API responses)
     */
    public String getStatusValue() {
        return status != null ? status.getValue() : BatchStatus.AVAILABLE.getValue();
    }
}

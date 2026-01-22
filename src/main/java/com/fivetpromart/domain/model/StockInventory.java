package com.fivetpromart.domain.model;

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
    private String status;

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
        stockInventory.status = "";

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
            String status
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
        stockInventory.status = status;
        return stockInventory;
    }

    public void update(
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long stockQuantity,
            BigDecimal importPrice,
            String status) {
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

        this.status = status;
    }
    
    /**
     * Update shelf and storage quantities
     * Per API Spec: quantityStorage + quantityShelf = stockQuantity
     */
    public void updateShelfStorage(Long newQuantityShelf, Long newQuantityStorage) {
        if (newQuantityShelf != null) {
            this.quantityShelf = newQuantityShelf;
        }
        if (newQuantityStorage != null) {
            this.quantityStorage = newQuantityStorage;
        }
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
     * Reduces both reserved and total stock
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
        this.reservedQuantity = currentReserved - quantity;
        this.stockQuantity = stockQuantity - quantity;
    }
}

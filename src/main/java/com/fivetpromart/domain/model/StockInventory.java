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
    private Long reservedQuantity; // NEW: Track how much is currently reserved
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
        stockInventory.reservedQuantity = 0L; // NEW: Initialize reserved to 0
        stockInventory.importPrice = importPrice;
        stockInventory.status = "";

        return stockInventory;
    }

    public static StockInventory reconstitute(
            String lotId,
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long stockQuantity, // FIXED: Restore stockQuantity parameter
            Long reservedQuantity, // NEW: Add reserved quantity
            BigDecimal importPrice,
            String status
    ) {
        StockInventory stockInventory = new StockInventory();
        stockInventory.lotId = lotId;
        stockInventory.productId = productId;
        stockInventory.manufactureDate = manufactureDate;
        stockInventory.expirationDate = expirationDate;
        stockInventory.stockQuantity = stockQuantity;
        stockInventory.reservedQuantity = reservedQuantity != null ? reservedQuantity : 0L; // NEW: Default to 0
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

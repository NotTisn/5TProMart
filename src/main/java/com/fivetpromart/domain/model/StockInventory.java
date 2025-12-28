package com.fivetpromart.domain.model;

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
    private BigDecimal importPrice;
    private String status;

    public static StockInventory create(String productId, LocalDate manufactureDate, LocalDate expirationDate, Long stockQuantity, BigDecimal importPrice, String status) {
        if(productId == null || productId.isBlank())
            throw new IllegalArgumentException("productId is null or empty");
        if(manufactureDate == null)
            throw new IllegalArgumentException("manufactureDate is null or empty");
        if(expirationDate == null)
            throw new IllegalArgumentException("expirationDate is null or empty");
        if(stockQuantity == null)
            throw new IllegalArgumentException("stockQuantity is null or empty");
        if(importPrice == null)
            throw new IllegalArgumentException("importPrice is null or empty");
        if(expirationDate.isBefore(manufactureDate))
            throw new IllegalArgumentException("Invalid expirationDate");

        StockInventory stockInventory = new StockInventory();
        if(status == null)
            stockInventory.status = "GOOD";

        stockInventory.lotId = UUID.randomUUID().toString();
        stockInventory.productId = productId;
        stockInventory.manufactureDate = manufactureDate;
        stockInventory.expirationDate = expirationDate;
        stockInventory.stockQuantity = stockQuantity;
        stockInventory.importPrice = importPrice;
        stockInventory.status = status;

        return stockInventory;
    }

    public static StockInventory reconstitute(
            String lotId,
            String productId,
            LocalDate manufactureDate,
            LocalDate expirationDate,
            Long stockQuantity,
            BigDecimal importPrice,
            String status
    ) {
        StockInventory stockInventory = new StockInventory();
        stockInventory.lotId = lotId;
        stockInventory.productId = productId;
        stockInventory.manufactureDate = manufactureDate;
        stockInventory.expirationDate = expirationDate;
        stockInventory.stockQuantity = stockQuantity;
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
}

package com.fivetpromart.domain.exception;

public class StockInventoryNotFoundException extends RuntimeException {
    public StockInventoryNotFoundException(String lotId) {
        super("Stock inventory not found: " + lotId);
    }
}

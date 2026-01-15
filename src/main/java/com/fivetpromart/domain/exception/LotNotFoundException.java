package com.fivetpromart.domain.exception;

public class LotNotFoundException extends RuntimeException {
    public LotNotFoundException(String lotId) {
        super("Lot not found with ID: " + lotId);
    }
}

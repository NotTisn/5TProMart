package com.fivetpromart.domain.exception;

public class ExpiredLotException extends DomainException {
    public ExpiredLotException(String lotId) {
        super("Lot has expired: " + lotId);
    }
}

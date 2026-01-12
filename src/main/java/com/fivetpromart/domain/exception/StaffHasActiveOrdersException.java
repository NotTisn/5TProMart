package com.fivetpromart.domain.exception;

public class StaffHasActiveOrdersException extends RuntimeException {
    public StaffHasActiveOrdersException(String staffId) {
        super("Staff " + staffId + " is currently handling import/selling orders. Cannot delete.");
    }
}

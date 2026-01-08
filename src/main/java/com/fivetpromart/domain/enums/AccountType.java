package com.fivetpromart.domain.enums;

public enum AccountType {
    ADMIN("Admin"),
    SALES_STAFF("SalesStaff"),
    WAREHOUSE_STAFF("WarehouseStaff");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AccountType fromString(String text) {
        for (AccountType b : AccountType.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + text);
    }
}
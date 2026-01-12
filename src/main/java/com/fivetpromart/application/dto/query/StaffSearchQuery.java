package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StaffSearchQuery {
    private String search;        // Search in fullName, phoneNumber, userId
    private String accountType;   // Filter by account type: "SalesStaff", "WarehouseStaff"
}

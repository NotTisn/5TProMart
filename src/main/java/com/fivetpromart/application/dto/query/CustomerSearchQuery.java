package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CustomerSearchQuery {
    private String customerId;
    private String customerName;
}

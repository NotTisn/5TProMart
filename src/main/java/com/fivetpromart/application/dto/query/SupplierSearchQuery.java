package com.fivetpromart.application.dto.query;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class SupplierSearchQuery {
    // SEARCH: Tìm kiếm trong supplierName HOẶC supplierId
    private String search;
    
    // FILTERS: Lọc theo các tiêu chí cụ thể
    private String supplierType;           // Doanh nghiệp / Tư nhân
    private String phoneNumber;            // Số điện thoại
    private String address;                // Địa chỉ (contains)
}

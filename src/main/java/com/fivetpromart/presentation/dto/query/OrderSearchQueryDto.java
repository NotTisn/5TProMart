package com.fivetpromart.presentation.dto.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchQueryDto {
    private String search;          // orderId, customerName, customerId
    private String staffId;
    private String startDate;       // dd-MM-yyyy
    private String endDate;         // dd-MM-yyyy
    private String paymentMethod;   // "Tiền mặt", "Chuyển khoản"
    private String status;          // "Đã thanh toán", "Chưa thanh toán", "Đã huỷ"
    private Integer page;
    private Integer size;
    private String sort;
}

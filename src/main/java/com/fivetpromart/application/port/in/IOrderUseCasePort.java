package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CheckProductResultDto;
import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.application.dto.command.CheckProductCommand;
import com.fivetpromart.application.dto.command.OrderCreationCommand;
import com.fivetpromart.application.dto.query.OrderSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderUseCasePort {
    /**
     * Search orders with filters and pagination
     */
    Page<OrderDto> searchOrders(OrderSearchQuery query, Pageable pageable);

//    /**
//     * Get order detail by ID
//     */
//    OrderDto getOrderById(String orderId);
//
    /**
     * Check product by scanning lot code
     */
    CheckProductResultDto checkProduct(CheckProductCommand command);

    /**
     * Create order (checkout)
     */
    OrderDto createOrder(OrderCreationCommand command);
}

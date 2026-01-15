package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CheckProductResultDto;
import com.fivetpromart.application.dto.OrderDto;
import com.fivetpromart.application.dto.command.CheckProductCommand;
import com.fivetpromart.application.dto.command.OrderCreationCommand;
import com.fivetpromart.application.dto.query.OrderSearchQuery;
import com.fivetpromart.application.port.in.IOrderUseCasePort;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.presentation.dto.query.OrderSearchQueryDto;
import com.fivetpromart.presentation.dto.request.CheckProductRequest;
import com.fivetpromart.presentation.dto.request.OrderRequest;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.OrderPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final IOrderUseCasePort orderUseCase;
    private final OrderPresentationMapper mapper;

    /**
     * 1.1 Get orders query
     * GET /api/v1/orders
     */
//    @GetMapping
//    @PreAuthorize("hasRole('Admin')")
//    public ApiResponse<List<OrderResponse>> searchOrders(
//            @RequestParam(required = false) String search,
//            @RequestParam(required = false) String staffId,
//            @RequestParam(required = false) String startDate,  // dd-MM-yyyy
//            @RequestParam(required = false) String endDate,    // dd-MM-yyyy
//            @RequestParam(required = false) String paymentMethod,
//            @RequestParam(required = false) String status,
//            @PageableDefault(size = 10, sort = "orderDate,desc") Pageable pageable
//    ) {
//        log.info("Searching orders with filters - search: {}, staffId: {}, dateRange: {} to {}",
//                search, staffId, startDate, endDate);
//
//        // Build query DTO
//        OrderSearchQueryDto queryDto = OrderSearchQueryDto.builder()
//                .search(search)
//                .staffId(staffId)
//                .startDate(startDate)
//                .endDate(endDate)
//                .paymentMethod(paymentMethod)
//                .status(status)
//                .build();
//
//        // Convert to domain query
//        OrderSearchQuery query = mapper.toOrderSearchQuery(queryDto);
//
//        // Call use case
//        Page<OrderDto> pageResult = orderUseCase.searchOrders(query, pageable);
//
//        // Map to response
//        List<OrderResponse> responseList = pageResult.getContent().stream()
//                .map(mapper::toOrderResponse)
//                .collect(Collectors.toList());
//
//        // Build pagination meta
//        PaginationMeta meta = PaginationMeta.builder()
//                .totalItems(pageResult.getTotalElements()) // Tổng số bản ghi
//                .itemsPerPage(pageResult.getSize())        // Kích thước trang
//                .totalPages(pageResult.getTotalPages())    // Tổng số trang
//                .startPage(pageResult.getNumber() + 1)     // QUAN TRỌNG: Spring bắt đầu từ 0, bạn muốn 1 thì phải +1
//                .build();
//
//        return ApiResponse.<List<OrderResponse>>builder()
//                .success(true)
//                .message("Get order list successfully.")
//                .data(responseList)
//                .pagination(meta)
//                .build();
//    }
//
//    /**
//     * 1.2 Get order detail
//     * GET /api/v1/orders/{id}
//     */
//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('Admin')")
//    public ApiResponse<OrderDetailResponse> getOrderById(@PathVariable String id) {
//        log.info("Getting order detail for ID: {}", id);
//
//        // Call use case
//        OrderDto orderDto = orderUseCase.getOrderById(id);
//
//        // Map to detail response
//        OrderDetailResponse response = mapper.toOrderDetailResponse(orderDto);
//
//        return ApiResponse.<OrderDetailResponse>builder()
//                .success(true)
//                .message("Get order detail successfully.")
//                .data(response)
//                .build();
//    }
//
//    /**
//     * 1.3 Check product (scan product code)
//     * POST /api/v1/orders/check-product
//     */
//    @PostMapping("/check-product")
//    @PreAuthorize("hasRole('Admin') or hasRole('SalesStaff')")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ApiResponse<CheckProductResponse> checkProduct(
//            @Valid @RequestBody CheckProductRequest request
//    ) {
//        log.info("Checking product for lotId: {}, quantity: {}",
//                request.getLotId(), request.getQuantity());
//
//        // Convert to command
//        CheckProductCommand command = mapper.toCheckProductCommand(request);
//
//        // Call use case
//        CheckProductResultDto resultDto = orderUseCase.checkProduct(command);
//
//        // Map to response
//        CheckProductResponse response = mapper.toCheckProductResponse(resultDto);
//
//        return ApiResponse.<CheckProductResponse>builder()
//                .success(true)
//                .message("Item check successfully.")
//                .data(response)
//                .build();
//    }

    /**
     * 1.4 Create Order (Checkout)
     * POST /api/v1/orders
     */
    @PostMapping
    //@PreAuthorize("hasRole('Admin') or hasRole('SalesStaff')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderCreationResponse> createOrder(
            @Valid @RequestBody OrderRequest request
    ) {
        String currentStaffId = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Creating order for staffId: {}, customerId: {}, items count: {}",
                currentStaffId, request.getCustomerId(), request.getItems().size());
        log.info("=== Authentication Debug ===");
        log.info("Authentication: {}", authentication);
        log.info("Authentication class: {}", authentication != null ? authentication.getClass().getName() : "null");
        log.info("Principal: {}", authentication != null ? authentication.getPrincipal() : "null");
        log.info("Name: {}", authentication != null ? authentication.getName() : "null");
        log.info("Authorities: {}", authentication != null ? authentication.getAuthorities() : "null");
        log.info("Is Authenticated: {}", authentication != null ? authentication.isAuthenticated() : "false");
        log.info("==========================");
        // Convert to command
        OrderCreationCommand command = mapper.toOrderCreationCommand(request, currentStaffId);
        command.setStaffId(currentStaffId);

        // Call use case
        OrderDto orderDto = orderUseCase.createOrder(command);

        // Map to creation response
        OrderCreationResponse response = mapper.toOrderCreationResponse(orderDto);

        return ApiResponse.<OrderCreationResponse>builder()
                .success(true)
                .message("Order created")
                .data(response)
                .build();
    }
}

package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.PurchaseOrderDto;
import com.fivetpromart.application.dto.command.PurchaseOrderCancelCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderConfirmCommand;
import com.fivetpromart.application.dto.command.PurchaseOrderCreationCommand;
import com.fivetpromart.application.dto.query.PurchaseOrderSearchQuery;
import com.fivetpromart.application.port.in.IPurchaseOrderUseCasePort;
import com.fivetpromart.presentation.dto.request.PurchaseOrderCancelRequest;
import com.fivetpromart.presentation.dto.request.PurchaseOrderConfirmRequest;
import com.fivetpromart.presentation.dto.request.PurchaseOrderCreationRequest;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.PurchaseOrderPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchase_orders")
@RequiredArgsConstructor
@Slf4j
public class PurchaseOrderController {

    private final IPurchaseOrderUseCasePort purchaseOrderUseCase;
    private final PurchaseOrderPresentationMapper mapper;

    /**
     * 2. Get purchase orders list
     * GET /api/v1/purchase_orders
     */
    @GetMapping
    public ApiResponse<List<PurchaseOrderListResponse>> getPurchaseOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String supplierId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "purchaseDate") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        log.info("Getting purchase orders list");

        // Build query
        PurchaseOrderSearchQuery query = PurchaseOrderSearchQuery.builder()
                .search(search)
                .supplierId(supplierId)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .sortBy(sortBy)
                .order(order)
                .build();

        // Call use case
        Pageable pageable = PageRequest.of(page, size);
        Page<PurchaseOrderDto> dtoPage = purchaseOrderUseCase.searchPurchaseOrders(query, pageable);

        // Map to response
        List<PurchaseOrderListResponse> responses = dtoPage.getContent().stream()
                .map(mapper::toListResponse)
                .collect(Collectors.toList());

        // Build pagination
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .totalItems(dtoPage.getTotalElements())
                .itemsPerPage(dtoPage.getSize())
                .totalPages(dtoPage.getTotalPages())
                .startPage(dtoPage.getNumber() + 1)
                .build();

        return ApiResponse.<List<PurchaseOrderListResponse>>builder()
                .success(true)
                .message("Get list successfully.")
                .data(responses)
                .pagination(paginationMeta)
                .build();
    }

    /**
     * 3. Get purchase order detail
     * GET /api/v1/purchase_orders/{id}
     */
    @GetMapping("/{id}")
    public ApiResponse<PurchaseOrderDetailResponse> getPurchaseOrderById(@PathVariable String id) {
        log.info("Getting purchase order detail: {}", id);

        PurchaseOrderDto dto = purchaseOrderUseCase.getPurchaseOrderById(id);
        PurchaseOrderDetailResponse response = mapper.toDetailResponse(dto);

        return ApiResponse.<PurchaseOrderDetailResponse>builder()
                .success(true)
                .message("Get detail successfully.")
                .data(response)
                .build();
    }

    /**
     * 4. Create Draft Purchase Order
     * POST /api/v1/purchase_orders
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PurchaseOrderCreationResponse> createDraftPurchaseOrder(
            @Valid @RequestBody PurchaseOrderCreationRequest request
    ) {
        log.info("Creating draft purchase order");

        // Convert to command
        PurchaseOrderCreationCommand command = mapper.toCreationCommand(request);

        // Call use case
        PurchaseOrderDto dto = purchaseOrderUseCase.createDraftPurchaseOrder(command);

        // Map to response
        PurchaseOrderCreationResponse response = mapper.toCreationResponse(dto);

        return ApiResponse.<PurchaseOrderCreationResponse>builder()
                .success(true)
                .message("Created draft purchase orders.")
                .data(response)
                .build();
    }

    /**
     * 1.4 Confirm Orders And Generate Lots Id
     * POST /api/v1/purchase_orders/{id}/confirm
     */
    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PurchaseOrderConfirmResponse> confirmPurchaseOrder(
            @PathVariable String id,
            @Valid @RequestBody PurchaseOrderConfirmRequest request
    ) {
        log.info("Confirming purchase order: {}", id);

        // Convert to command
        PurchaseOrderConfirmCommand command = mapper.toConfirmCommand(request);

        // Call use case
        PurchaseOrderDto dto = purchaseOrderUseCase.confirmPurchaseOrder(id, command);

        // Get lots to print
        List<PurchaseOrderDto.LotToPrintDto> lotsToPrint = purchaseOrderUseCase.getLabelsForReprint(id);

        // Build response
        PurchaseOrderConfirmResponse response = PurchaseOrderConfirmResponse.builder()
                .poCode(dto.getPoCode())
                .status(dto.getStatus())
                .checkDate(dto.getCheckDate())
                .finalTotalAmount(dto.getTotalAmount())
                .lotsToPrint(lotsToPrint.stream()
                        .map(mapper::toLotToPrintResponse)
                        .collect(Collectors.toList()))
                .build();

        return ApiResponse.<PurchaseOrderConfirmResponse>builder()
                .success(true)
                .message("Order confirmed. Stock Inventory updated.")
                .data(response)
                .build();
    }

    /**
     * 1.5 Cancel orders
     * POST /api/v1/purchase_orders/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PurchaseOrderCancelResponse> cancelPurchaseOrder(
            @PathVariable String id,
            @Valid @RequestBody PurchaseOrderCancelRequest request
    ) {
        log.info("Cancelling purchase order: {}", id);

        // Convert to command
        PurchaseOrderCancelCommand command = mapper.toCancelCommand(request);

        // Call use case
        PurchaseOrderDto dto = purchaseOrderUseCase.cancelPurchaseOrder(id, command);

        // Map to response
        PurchaseOrderCancelResponse response = mapper.toCancelResponse(dto);

        return ApiResponse.<PurchaseOrderCancelResponse>builder()
                .success(true)
                .message("Purchase order cancelled successfully.")
                .data(response)
                .build();
    }

    /**
     * 1.6 Reprint labels
     * GET /api/v1/purchase_orders/{id}/labels
     */
    @GetMapping("/{id}/labels")
    public ApiResponse<List<LotToPrintResponse>> getLabelsForReprint(@PathVariable String id) {
        log.info("Getting labels for reprint: {}", id);

        List<PurchaseOrderDto.LotToPrintDto> lotsToPrint = purchaseOrderUseCase.getLabelsForReprint(id);

        List<LotToPrintResponse> responses = lotsToPrint.stream()
                .map(mapper::toLotToPrintResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<LotToPrintResponse>>builder()
                .success(true)
                .message("Get labels successfully.")
                .data(responses)
                .build();
    }
}

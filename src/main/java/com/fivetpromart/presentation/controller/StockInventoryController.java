package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.port.in.IStockInventoryUseCasePort;
import com.fivetpromart.presentation.dto.request.StockInventoryRequest;
import com.fivetpromart.presentation.dto.request.StockInventoryUpdateRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.PaginationMeta;
import com.fivetpromart.presentation.dto.response.StockInventoryResponse;
import com.fivetpromart.presentation.mapper.StockInventoryPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stock-inventories")
@RequiredArgsConstructor
@Slf4j
public class StockInventoryController {

    private final IStockInventoryUseCasePort stockInventoryUseCase;
    private final StockInventoryPresentationMapper mapper;

    /**
     * 5.1 Get all stock inventory query
     * GET /api/stock-inventories
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_Admin') or hasRole('WarehouseStaff')")
    public ApiResponse<List<StockInventoryResponse>> searchStockInventories(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "expirationDate") String sortBy,  // expirationDate, stockQuantity, importPrice
            @RequestParam(defaultValue = "asc") String order,    // asc, desc
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Searching stock inventories with filters - search: {}, productId: {}, status: {}, page: {}, size: {}",
                search, productId, status, page, size);

        // Build query
        StockInventorySearchQuery query = mapper.toSearchQuery(search, productId, status, sortBy, order);

        // Build pageable
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        // Call use case
        Page<StockInventoryDto> dtoPage = stockInventoryUseCase.searchStockInventories(query, pageable);

        // Map to response
        List<StockInventoryResponse> responseList = dtoPage.getContent().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        // Build pagination meta
        PaginationMeta paginationMeta = PaginationMeta.builder()
                .totalItems(dtoPage.getTotalElements())
                .itemsPerPage(dtoPage.getSize())
                .totalPages(dtoPage.getTotalPages())
                .startPage(dtoPage.getNumber() + 1)
                .build();

        return ApiResponse.<List<StockInventoryResponse>>builder()
                .success(true)
                .message("Stock inventories retrieved successfully")
                .data(responseList)
                .pagination(paginationMeta)
                .build();
    }

    /**
     * 5.2 Get stock inventory by ID
     * GET /api/stock-inventories/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WarehouseStaff')")
    public ApiResponse<StockInventoryResponse> getStockInventoryById(@PathVariable String id) {
        log.info("Getting stock inventory by ID: {}", id);

        // Call use case
        StockInventoryDto dto = stockInventoryUseCase.getStockInventoryById(id);

        // Map to response
        StockInventoryResponse response = mapper.toResponse(dto);

        return ApiResponse.<StockInventoryResponse>builder()
                .success(true)
                .message("Stock inventory retrieved successfully")
                .data(response)
                .build();
    }

    /**
     * 5.3 Add new stock inventory
     * POST /api/stock-inventories
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WarehouseStaff')")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StockInventoryResponse> createStockInventory(
            @Valid @RequestBody StockInventoryRequest request
    ) {
        log.info("Creating stock inventory for productId: {}", request.getProductId());

        // Convert to command
        StockInventoryCreationCommand command = mapper.toCreationCommand(request);

        // Call use case
        StockInventoryDto dto = stockInventoryUseCase.createStockInventory(command);

        // Map to response
        StockInventoryResponse response = mapper.toResponse(dto);

        return ApiResponse.<StockInventoryResponse>builder()
                .success(true)
                .message("Stock inventory created successfully")
                .data(response)
                .build();
    }

    /**
     * 5.4 Update stock inventory
     * PUT /api/stock-inventories/{lot_id}
     */
    @PutMapping("/{lot_id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WarehouseStaff')")
    public ApiResponse<StockInventoryResponse> updateStockInventory(
            @PathVariable("lot_id") String lotId,
            @Valid @RequestBody StockInventoryUpdateRequest request
    ) {
        log.info("Updating stock inventory: {}", lotId);

        // Convert to command
        StockInventoryUpdateCommand command = mapper.toUpdateCommand(request);

        // Call use case
        StockInventoryDto dto = stockInventoryUseCase.updateStockInventory(lotId, command);

        // Map to response (only return updated fields as per spec)
        StockInventoryResponse response = StockInventoryResponse.builder()
                .lotId(dto.getLotId())
                .stockQuantity(dto.getStockQuantity())
                .status(dto.getStatus())
                .build();

        return ApiResponse.<StockInventoryResponse>builder()
                .success(true)
                .message("Stock inventory updated.")
                .data(response)
                .build();
    }

    /**
     * Delete stock inventory (not in spec, but commonly needed)
     * DELETE /api/stock-inventories/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockInventory(@PathVariable String id) {
        log.info("Deleting stock inventory: {}", id);
        stockInventoryUseCase.deleteById(id);
    }
}

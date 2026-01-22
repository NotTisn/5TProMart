package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.DisposalBatchResultDto;
import com.fivetpromart.application.dto.DisposeLotResultDto;
import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.DisposalBatchCommand;
import com.fivetpromart.application.dto.command.DisposeLotCommand;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.port.in.IStockInventoryUseCasePort;
import com.fivetpromart.presentation.dto.request.DisposalBatchRequest;
import com.fivetpromart.presentation.dto.request.DisposeLotRequest;
import com.fivetpromart.presentation.dto.request.StockInventoryRequest;
import com.fivetpromart.presentation.dto.request.StockInventoryUpdateRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.DisposalBatchResponse;
import com.fivetpromart.presentation.dto.response.DisposeLotResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stock_inventories")
@RequiredArgsConstructor
@Slf4j
public class StockInventoryController {

    private final IStockInventoryUseCasePort stockInventoryUseCase;
    private final StockInventoryPresentationMapper mapper;

    /**
     * 5.1 Get all stock inventory query with pagination
     * GET /api/stock-inventories
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
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
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
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
    @PreAuthorize("hasAnyRole('Admin', 'WarehouseStaff')")
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
    @PreAuthorize("hasAnyRole('Admin', 'WarehouseStaff')")
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
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStockInventory(@PathVariable String id) {
        log.info("Deleting stock inventory: {}", id);
        stockInventoryUseCase.deleteById(id);
    }

    /**
     * 5.5 Batch Disposal Stock Inventory (spec compliant)
     * POST /api/v1/inventory/disposal
     */
    @PostMapping("/disposal")
    @PreAuthorize("hasAnyRole('Admin', 'WarehouseStaff')")
    public ApiResponse<DisposalBatchResponse> createDisposalBatch(
            @Valid @RequestBody DisposalBatchRequest request
    ) {
        log.info("Creating disposal batch with {} items", request.getItems().size());

        String currentStaffId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Convert to command and call use case
        DisposalBatchCommand command = mapper.toDisposalBatchCommand(request);
        command.setStaffId(currentStaffId);
        DisposalBatchResultDto resultDto = stockInventoryUseCase.createDisposalBatch(command);

        // Map to response
        DisposalBatchResponse response = DisposalBatchResponse.builder()
                .disposalId(resultDto.getDisposalId())
                .staffId(resultDto.getStaffId())
                .date(resultDto.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
                .totalItems(resultDto.getTotalItems())
                .build();

        return ApiResponse.<DisposalBatchResponse>builder()
                .success(true)
                .message("Disposal created successfully.")
                .data(response)
                .build();
    }

    /**
     * Single lot disposal (legacy endpoint, kept for backward compatibility)
     * POST /api/v1/stock_inventories/{lotId}/dispose
     */
    @PostMapping("/{lotId}/dispose")
    @PreAuthorize("hasAnyRole('Admin', 'WarehouseStaff')")
    public ApiResponse<DisposeLotResponse> disposeLot(
            @PathVariable String lotId,
            @Valid @RequestBody DisposeLotRequest request
    ) {
        log.info("Disposing lot: {} with quantity: {}", lotId, request.getQuantity());

        String currentStaffId = SecurityContextHolder.getContext().getAuthentication().getName();

        // Build command
        DisposeLotCommand command = DisposeLotCommand.builder()
                .lotId(lotId)
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .notes(request.getNotes())
                .staffId(currentStaffId)
                .build();

        // Call use case
        DisposeLotResultDto resultDto = stockInventoryUseCase.disposeLot(command);

        // Map to response
        DisposeLotResponse response = DisposeLotResponse.builder()
                .disposalId(resultDto.getDisposalId())
                .lotId(resultDto.getLotId())
                .productId(resultDto.getProductId())
                .productName(resultDto.getProductName())
                .quantityDisposed(resultDto.getQuantityDisposed())
                .remainingLotQuantity(resultDto.getRemainingLotQuantity())
                .productTotalStock(resultDto.getProductTotalStock())
                .disposedAt(resultDto.getDisposedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .disposedBy(resultDto.getDisposedBy())
                .reason(resultDto.getReason())
                .notes(resultDto.getNotes())
                .build();

        return ApiResponse.<DisposeLotResponse>builder()
                .success(true)
                .message("Lot disposed successfully")
                .data(response)
                .build();
    }
}

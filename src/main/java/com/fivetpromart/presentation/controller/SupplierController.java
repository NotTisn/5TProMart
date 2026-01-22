package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.SuppliedProductDto;
import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import com.fivetpromart.application.usecase.ProductUseCase;
import com.fivetpromart.application.usecase.SupplierUseCase;
import com.fivetpromart.infrastructure.persistence.supplier.mapper.SupplierPersistenceMapper;
import com.fivetpromart.presentation.dto.request.SupplierRequest;
import com.fivetpromart.presentation.dto.response.*;
import com.fivetpromart.presentation.mapper.SupplierPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;
    private final SupplierPresentationMapper mapper;
    private final ProductUseCase productUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<SupplierResponse> addNewSupplier (
            @Valid @RequestBody SupplierRequest request
    ) {
        SupplierCreationCommand command = mapper.toCreateCommand(request);
        SupplierDto dto = supplierUseCase.createSupplier(command);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully added new supplier")
                .data(mapper.toResponse(dto))
                .build();
    }

    @PutMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.OK)
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<SupplierResponse> updateSupplier (
            @PathVariable String supplierId,
            @Valid @RequestBody SupplierRequest request
    ) {
        SupplierUpdateCommand command = mapper.toUpdateCommand(request);
        command = command.toBuilder()
                    .supplierId(supplierId)
                    .build();

        SupplierDto dto = supplierUseCase.updateSupplier(command);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update supplier")
                .data(mapper.toResponse(dto))
                .build();
    }

    @GetMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<SupplierResponse> getSupplierById (
            @PathVariable String supplierId
    ) {
        SupplierDto dto = supplierUseCase.getSupplierById(supplierId);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update supplier")
                .data(mapper.toResponse(dto))
                .build();
    }

    @DeleteMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse deleteSupplierById (
            @PathVariable String supplierId
    ) {
        supplierUseCase.deleteSupplierById(supplierId);

        return ApiResponse.builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully deleted supplier")
                .build();
    }

    @PostMapping("/{supplierId}/restore")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<SupplierResponse> restoreSupplier(
            @PathVariable String supplierId
    ) {
        SupplierDto dto = supplierUseCase.restoreSupplier(supplierId);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully restored supplier")
                .data(mapper.toResponse(dto))
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<List<SupplierResponse>> getAllSuppliersByPage(
            // SEARCH: Tìm kiếm trong supplierName hoặc supplierId
            @RequestParam(required = false) String search,
            
            // FILTERS: Lọc theo các tiêu chí cụ thể
            @RequestParam(required = false) String supplierType,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
            
            @PageableDefault(size = 10) Pageable pageable
    ) {
        SupplierSearchQuery query = SupplierSearchQuery.builder()
                .search(search)
                .supplierType(supplierType)
                .phoneNumber(phoneNumber)
                .address(address)
                .includeDeleted(includeDeleted)
                .build();
                
        Page<SupplierDto> pageResult = supplierUseCase.getAllSuppliers(query, pageable);

        List<SupplierResponse> responses = pageResult.stream()
                .map(mapper::toResponse)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .totalItems(pageResult.getTotalElements()) // Tổng số bản ghi
                .itemsPerPage(pageResult.getSize())        // Kích thước trang
                .totalPages(pageResult.getTotalPages())    // Tổng số trang
                .startPage(pageResult.getNumber() + 1)     // QUAN TRỌNG: Spring bắt đầu từ 0, bạn muốn 1 thì phải +1
                .build();

        // 4. Trả về kết quả gộp
        return ApiResponse.<List<SupplierResponse>>builder()
                .success(true)
                .statusCode(200)
                .message("Get suppliers successfully")
                .data(responses)  // Mảng dữ liệu
                .pagination(meta)    // Thông tin phân trang
                .build();
    }

    /**
     * 1.6 Get supplier's products
     * GET /api/v1/suppliers/{id}/products
     */
    @GetMapping("/{supplierId}/products")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'WarehouseStaff')")
    public ApiResponse<List<SupplierProductResponse>> getSupplierProducts(
            @PathVariable String supplierId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        // Get supplier to access suppliedProducts
        SupplierDto supplier = supplierUseCase.getSupplierById(supplierId);
        
        if (supplier.getSuppliedProducts() == null || supplier.getSuppliedProducts().isEmpty()) {
            return ApiResponse.<List<SupplierProductResponse>>builder()
                    .success(true)
                    .statusCode(200)
                    .message("Get supplier products successfully.")
                    .data(new ArrayList<>())
                    .pagination(PaginationMeta.builder()
                            .totalItems(0L)
                            .itemsPerPage(pageable.getPageSize())
                            .totalPages(0)
                            .startPage(1)
                            .build())
                    .build();
        }
        
        // Create a map for quick lookup of supplied product info
        Map<String, SuppliedProductDto> suppliedProductMap = supplier.getSuppliedProducts().stream()
                .collect(Collectors.toMap(
                        SuppliedProductDto::getProductId,
                        sp -> sp
                ));
        
        // Fetch product details and combine with supplier info
        List<SupplierProductResponse> productResponses = new ArrayList<>();
        for (SuppliedProductDto suppliedProduct : supplier.getSuppliedProducts()) {
            try {
                ProductDto product = productUseCase.getProductById(suppliedProduct.getProductId());
                
                SupplierProductResponse response = SupplierProductResponse.builder()
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .unitOfMeasure(product.getUnitOfMeasure())
                        .totalStockQuantity(product.getTotalStockQuantity())
                        .lastImportPrice(suppliedProduct.getLastImportPrice())
                        .lastImportDate(suppliedProduct.getLastImportDate())
                        .build();
                
                productResponses.add(response);
            } catch (Exception e) {
                // Skip products that don't exist
                continue;
            }
        }
        
        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productResponses.size());
        List<SupplierProductResponse> paginatedList = productResponses.subList(start, end);
        
        PaginationMeta meta = PaginationMeta.builder()
                .totalItems((long) productResponses.size())
                .itemsPerPage(pageable.getPageSize())
                .totalPages((int) Math.ceil((double) productResponses.size() / pageable.getPageSize()))
                .startPage(pageable.getPageNumber())
                .build();
        
        return ApiResponse.<List<SupplierProductResponse>>builder()
                .success(true)
                .statusCode(200)
                .message("Get supplier products successfully.")
                .data(paginatedList)
                .pagination(meta)
                .build();
    }
}

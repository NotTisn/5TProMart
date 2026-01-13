package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.application.dto.query.SupplierSearchQuery;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;
    private final SupplierPresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<SupplierResponse>> getAllSuppliersByPage(
            // SEARCH: Tìm kiếm trong supplierName hoặc supplierId
            @RequestParam(required = false) String search,
            
            // FILTERS: Lọc theo các tiêu chí cụ thể
            @RequestParam(required = false) String supplierType,
            @RequestParam(required = false) String suppliedProductType,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String address,
            
            @PageableDefault(size = 10) Pageable pageable
    ) {
        SupplierSearchQuery query = SupplierSearchQuery.builder()
                .search(search)
                .supplierType(supplierType)
                .suppliedProductType(suppliedProductType)
                .phoneNumber(phoneNumber)
                .address(address)
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
}

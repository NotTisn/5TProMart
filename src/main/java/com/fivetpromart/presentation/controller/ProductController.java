package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.ProductStatsDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.application.usecase.ProductUseCase;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.PaginationMeta;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import com.fivetpromart.presentation.dto.response.ProductStatsResponse;
import com.fivetpromart.presentation.mapper.ProductPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductPresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        ProductCreationCommand product = mapper.toCommand(request);
        ProductDto dto = productUseCase.addNewProduct(product);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created new product")
                .data(mapper.toProductResponse(dto))
                .build();
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductRequest request
    ) {
        ProductUpdateCommand command = mapper.toUpdateCommand(request);
        command = command.toBuilder()
                    .productId(productId)
                    .build();

        ProductDto dto = productUseCase.updateProduct(command);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully updated a product")
                .data(mapper.toProductResponse(dto))
                .build();
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse deleteProduct(
            @PathVariable String productId
    ) {
        productUseCase.deleteProduct(productId);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully deleted a product")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'SalesStaff', 'WarehouseStaff')")
    public ApiResponse<List<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String productId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        // 1. Gọi UseCase (Nhận về Page của Spring)
        ProductSearchQuery query = ProductSearchQuery.builder()
                .productName(productName)
                .categoryId(categoryId)
                .productId(productId)
                .build();
        Page<ProductDto> pageResult = productUseCase.getAllProducts(query, pageable);

        // 2. Lấy List Data (data)
        List<ProductResponse> responseList = pageResult.stream()
                .map(mapper::toProductResponse)
                .toList();

        // 3. Tạo Pagination Meta (pagination)
        // Map từ thông số của Spring Page sang Object của bạn
        PaginationMeta meta = PaginationMeta.builder()
                .totalItems(pageResult.getTotalElements()) // Tổng số bản ghi
                .itemsPerPage(pageResult.getSize())        // Kích thước trang
                .totalPages(pageResult.getTotalPages())    // Tổng số trang
                .startPage(pageResult.getNumber() + 1)     // QUAN TRỌNG: Spring bắt đầu từ 0, bạn muốn 1 thì phải +1
                .build();

        // 4. Trả về kết quả gộp
        return ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .statusCode(200)
                .message("Get products successfully")
                .data(responseList)  // Mảng dữ liệu
                .pagination(meta)    // Thông tin phân trang
                .build();
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager', 'SalesStaff', 'WarehouseStaff')")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable String productId
    ) {
        ProductDto product = productUseCase.getProductById(productId);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .statusCode(200)
                .message("Get products successfully")
                .data(mapper.toProductResponse(product))
                .build();
    }

    /**
     * Get product and inventory statistics for dashboard
     * GET /api/v1/products/stats
     */
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<ProductStatsResponse> getProductStats() {
        ProductStatsDto statsDto = productUseCase.getProductStats();

        ProductStatsResponse response = ProductStatsResponse.builder()
                .totalProducts(statsDto.getTotalProducts())
                .activeProducts(statsDto.getActiveProducts())
                .inactiveProducts(statsDto.getInactiveProducts())
                .totalInventoryValue(statsDto.getTotalInventoryValue())
                .lowStockCount(statsDto.getLowStockCount())
                .outOfStockCount(statsDto.getOutOfStockCount())
                .expiringSoonCount(statsDto.getExpiringSoonCount())
                .expiredCount(statsDto.getExpiredCount())
                .build();

        return ApiResponse.<ProductStatsResponse>builder()
                .success(true)
                .statusCode(200)
                .message("Get product statistics successfully")
                .data(response)
                .build();
    }
}

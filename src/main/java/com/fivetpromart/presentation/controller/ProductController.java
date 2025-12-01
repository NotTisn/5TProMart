package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.dto.query.ProductSearchQuery;
import com.fivetpromart.application.usecase.ProductUseCase;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import com.fivetpromart.presentation.mapper.ProductPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductPresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
    public ApiResponse<List<ProductResponse>> getAllProducts(
            // 1. Nhóm Filter: Map thủ công vào DTO
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String productName,

            // 2. Nhóm Pagination & Sort: Spring tự làm
            // Hỗ trợ URL: ?page=0&size=10&sort=sellingPrice,desc
            @PageableDefault(size = 10, sort = "productName") Pageable pageable
    ) {
        // Build Filter DTO
        ProductSearchQuery query = ProductSearchQuery.builder()
                .productId(id)
                .categoryId(categoryId)
                .productName(productName)
                .build();

        // Truyền cả 2 vào UseCase
        Page<ProductDto> pageResult = productUseCase.getAllProducts(query, pageable);

        // map to response
        List<ProductResponse> productResponses = pageResult.stream()
                .map(mapper::toProductResponse)
                .toList();

        return ApiResponse.<List<ProductResponse>>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully retrieve list of products")
                .data(productResponses)
                .build();
    }
}

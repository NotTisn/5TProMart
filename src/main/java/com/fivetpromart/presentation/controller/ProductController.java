package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCreationCommand;
import com.fivetpromart.application.dto.command.ProductUpdateCommand;
import com.fivetpromart.application.usecase.ProductUseCase;
import com.fivetpromart.presentation.dto.request.ProductRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import com.fivetpromart.presentation.mapper.ProductPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}

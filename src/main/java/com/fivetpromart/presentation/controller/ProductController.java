package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.ProductDto;
import com.fivetpromart.application.dto.command.ProductCommand;
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

    ProductUseCase productUseCase;
    ProductPresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        ProductCommand product = mapper.toCommand(request);
        ProductDto dto = productUseCase.addNewProduct(product);

        return ApiResponse.<ProductResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully created new product")
                .data(mapper.toProductResponse(dto))
                .build();
    }
}

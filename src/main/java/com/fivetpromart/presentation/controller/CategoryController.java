package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CategoryDto;
import com.fivetpromart.application.usecase.CategoryUseCase;
import com.fivetpromart.presentation.dto.request.CategoryRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CategoryResponse;
import com.fivetpromart.presentation.mapper.CategoryPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;
    private final CategoryPresentationMapper mapper;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse categoryResponse = mapper.toResponse(
                categoryUseCase.addNewCategory(request.getCategoryName()));
        return ApiResponse.created(categoryResponse);
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryRequest request
    ) {
        CategoryDto dto = categoryUseCase.updateCategory(categoryId, request.getCategoryName());
        CategoryResponse categoryResponse = mapper.toResponse(dto);

        return ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Category updated successfully")
                .data(categoryResponse)
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryDto> dtos = categoryUseCase.findAllCategories();

        List<CategoryResponse> responses = dtos.stream()
                .map(mapper::toResponse)
                .toList();

        return ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Categories found")
                .data(responses)
                .build();
    }
}

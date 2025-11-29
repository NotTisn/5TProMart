package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CategoryDto;
import com.fivetpromart.application.usecase.CategoryUseCase;
import com.fivetpromart.presentation.dto.request.CategoryRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CategoryResponse;
import com.fivetpromart.presentation.mapper.CategoryPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class CategoryController {

    CategoryUseCase categoryUseCase;
    CategoryPresentationMapper mapper;

    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse categoryResponse = mapper.toResponse(
                categoryUseCase.addNewCategory(request.getCategoryName()));
        return ApiResponse.created(categoryResponse);
    }
}

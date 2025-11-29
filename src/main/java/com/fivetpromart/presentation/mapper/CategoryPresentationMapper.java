package com.fivetpromart.presentation.mapper;

import com.fivetpromart.application.dto.CategoryDto;
import com.fivetpromart.presentation.dto.response.CategoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPresentationMapper {
    CategoryResponse toResponse(CategoryDto category);
}

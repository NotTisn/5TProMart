package com.fivetpromart.application.mapper;

import com.fivetpromart.application.dto.CategoryDto;
import com.fivetpromart.domain.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDataMapper {
    CategoryDto ToDto(Category category);
}

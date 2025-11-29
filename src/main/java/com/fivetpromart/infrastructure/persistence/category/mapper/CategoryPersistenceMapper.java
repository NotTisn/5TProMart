package com.fivetpromart.infrastructure.persistence.category.mapper;

import com.fivetpromart.domain.model.Category;
import com.fivetpromart.infrastructure.persistence.category.CategoryDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryPersistenceMapper {

    default CategoryDbo toDbo(Category domain) {
        if (domain == null) return null;

        return CategoryDbo.builder()
                .categoryId(domain.getCategoryId())
                .categoryName(domain.getCategoryName())
                .build();
    }

    default Category toDomain(CategoryDbo dbo) {
        if (dbo == null) return null;

        return Category.reconstitute(
                dbo.getCategoryId(),
                dbo.getCategoryName()
        );
    }
}

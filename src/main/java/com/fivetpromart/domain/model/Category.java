package com.fivetpromart.domain.model;

import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private String categoryId;
    private String categoryName;

    public static Category create(String categoryName) {
        if (categoryName == null || categoryName.isEmpty())
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY);
        Category category = new Category();
        category.categoryId = UUID.randomUUID().toString();
        category.categoryName = categoryName;

        return category;
    }

    public static Category reconstitute(
            String id,
            String name
    ) {
        Category category = new Category();
        category.categoryId = id;
        category.categoryName = name;
        return category;
    }

    public void updateCategory(String categoryName) {
        if(categoryName != null && categoryName.isBlank()) {
            this.categoryName = categoryName;
        }
    }
}



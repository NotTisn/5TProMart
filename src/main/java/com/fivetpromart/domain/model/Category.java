package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    private String categoryId;
    private String categoryName;
    private Boolean isActive = true;

    public static Category create(String categoryName) {
        if (categoryName == null || categoryName.isEmpty())
            throw new EmptyFieldException("Category name");
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
        category.isActive = true;
        return category;
    }

    public void updateCategory(String categoryName) {
        if(categoryName != null && categoryName.isBlank()) {
            this.categoryName = categoryName;
        }
    }

    // Soft delete methods
    public void deactivate() {
        this.isActive = false;
    }

    public void activate() {
        this.isActive = true;
    }

    public boolean isActive() {
        return this.isActive != null && this.isActive;
    }
}



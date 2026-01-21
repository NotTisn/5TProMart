package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CategoryDto;

import java.util.List;

public interface ICategoryUseCasePort {
    CategoryDto addNewCategory(String categoryName);
    CategoryDto updateCategory(String categoryName, String categoryId);
    List<CategoryDto> findAllCategories();
    List<CategoryDto> findAllCategories(Boolean includeDeleted);
    CategoryDto findCategoryById(String categoryId);
    void deleteCategoryById(String categoryId);
    CategoryDto restoreCategory(String categoryId);
}

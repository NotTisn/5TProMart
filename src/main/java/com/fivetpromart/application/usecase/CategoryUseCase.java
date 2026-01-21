package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.CategoryDto;
import com.fivetpromart.application.mapper.CategoryDataMapper;
import com.fivetpromart.application.port.in.ICategoryUseCasePort;
import com.fivetpromart.application.port.out.ICategoryRepository;
import com.fivetpromart.domain.exception.CategoryNotFoundException;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.model.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryUseCase implements ICategoryUseCasePort {

    private final CategoryDataMapper mapper;
    private final ICategoryRepository categoryRepository;

    @Override
    public CategoryDto addNewCategory(String categoryName) {
        if(categoryName == null || categoryName.isBlank())
            throw new EmptyFieldException("Category name");

        Category category = Category.create(categoryName);
        Category savedCategory = categoryRepository.save(category);

        return mapper.ToDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(String categoryName, String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        category.updateCategory(categoryName);
        Category updatedCategory = categoryRepository.save(category);

        return mapper.ToDto(updatedCategory);
    }

    @Override
    public List<CategoryDto> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(mapper::ToDto)
                .toList();
    }

    @Override
    public List<CategoryDto> findAllCategories(Boolean includeDeleted) {
        List<Category> categories;
        if (includeDeleted != null && includeDeleted) {
            categories = categoryRepository.findAllIncludingDeleted();
        } else {
            categories = categoryRepository.findAll();
        }

        return categories.stream()
                .map(mapper::ToDto)
                .toList();
    }

    @Override
    public CategoryDto findCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return mapper.ToDto(category);
    }

    @Override
    public void deleteCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        categoryRepository.delete(category);
    }

    @Override
    public CategoryDto restoreCategory(String categoryId) {
        Category category = categoryRepository.findByIdIncludingDeleted(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        
        if (category.isActive()) {
            log.warn("Category {} is already active", categoryId);
        }
        
        category.activate();
        return mapper.ToDto(categoryRepository.save(category));
    }
}

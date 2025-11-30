package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Category;

import java.util.Optional;

public interface ICategoryRepository {
    Category save(Category category);

    Optional<Category> findById(String categoryId);
}

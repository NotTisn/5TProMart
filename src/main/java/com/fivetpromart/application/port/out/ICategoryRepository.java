package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepository {
    Category save(Category category);

    Optional<Category> findById(String categoryId);
    
    Optional<Category> findByIdIncludingDeleted(String categoryId);

    List<Category> findAll();
    
    List<Category> findAllIncludingDeleted();

    void delete(Category category);

    boolean existsById(String categoryId);
}

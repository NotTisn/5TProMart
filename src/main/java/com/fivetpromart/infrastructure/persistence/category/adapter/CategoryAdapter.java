package com.fivetpromart.infrastructure.persistence.category.adapter;

import com.fivetpromart.application.port.out.ICategoryRepository;
import com.fivetpromart.domain.model.Category;
import com.fivetpromart.infrastructure.persistence.category.CategoryDbo;
import com.fivetpromart.infrastructure.persistence.category.mapper.CategoryPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.category.repository.ICategoryJpaRepository;
import com.fivetpromart.infrastructure.persistence.customer.repository.ICustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryRepository {

    private final ICategoryJpaRepository categoryJpaRepository;
    private final CategoryPersistenceMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryDbo dbo = mapper.toDbo(category);
        CategoryDbo savedDbo = categoryJpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }
}

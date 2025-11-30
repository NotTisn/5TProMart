package com.fivetpromart.infrastructure.persistence.category.repository;

import com.fivetpromart.infrastructure.persistence.category.CategoryDbo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryJpaRepository extends JpaRepository<CategoryDbo, String> {


}

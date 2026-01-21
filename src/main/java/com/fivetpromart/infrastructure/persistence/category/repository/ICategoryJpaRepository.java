package com.fivetpromart.infrastructure.persistence.category.repository;

import com.fivetpromart.infrastructure.persistence.category.CategoryDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryJpaRepository extends JpaRepository<CategoryDbo, String> {
    
    // Find only active categories
    @Query("SELECT c FROM CategoryDbo c WHERE c.isActive = true")
    List<CategoryDbo> findAllActive();
    
    @Query("SELECT c FROM CategoryDbo c WHERE c.isActive = true")
    Page<CategoryDbo> findAllActive(Pageable pageable);
    
    // Find active category by ID
    Optional<CategoryDbo> findByCategoryIdAndIsActiveTrue(String categoryId);
    
    // Find active category by name
    Optional<CategoryDbo> findByCategoryNameAndIsActiveTrue(String categoryName);
    
    // Check if active category exists by name
    boolean existsByCategoryNameAndIsActiveTrue(String categoryName);
    
    // Check if active category exists by name (excluding specific ID)
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoryDbo c " +
           "WHERE c.categoryName = :categoryName AND c.categoryId != :categoryId AND c.isActive = true")
    boolean existsByCategoryNameAndCategoryIdNotAndIsActiveTrue(
            @Param("categoryName") String categoryName,
            @Param("categoryId") String categoryId);
    
    // Search active categories
    @Query("SELECT c FROM CategoryDbo c WHERE c.isActive = true AND " +
           "LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CategoryDbo> searchActiveCategories(@Param("search") String search, Pageable pageable);
}

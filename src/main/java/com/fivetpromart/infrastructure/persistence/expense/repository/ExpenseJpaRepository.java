package com.fivetpromart.infrastructure.persistence.expense.repository;

import com.fivetpromart.infrastructure.persistence.expense.entity.ExpenseDbo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseJpaRepository extends JpaRepository<ExpenseDbo, String> {

    @Query("""
            SELECT e FROM ExpenseDbo e
            WHERE (:startDate IS NULL OR e.payDate >= :startDate)
            AND (:endDate IS NULL OR e.payDate <= :endDate)
            AND (:search IS NULL OR :search = '' OR 
                 LOWER(e.category) LIKE LOWER(CONCAT('%', :search, '%')) OR 
                 LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<ExpenseDbo> findExpensesByFilters(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("search") String search,
            Pageable pageable
    );

    @Query("""
            SELECT e.category as categoryName, SUM(e.amount) as totalAmount
            FROM ExpenseDbo e
            WHERE e.payDate >= :startDate AND e.payDate <= :endDate
            GROUP BY e.category
            """)
    List<CategoryBreakdownProjection> findCategoryBreakdown(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    interface CategoryBreakdownProjection {
        String getCategoryName();
        java.math.BigDecimal getTotalAmount();
    }
}

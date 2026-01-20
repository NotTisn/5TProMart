package com.fivetpromart.infrastructure.persistence.expense.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDbo {

    @Id
    @Column(name = "expense_id", nullable = false, length = 36)
    private String expenseId;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "pay_date", nullable = false)
    private LocalDate payDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "expense_images", joinColumns = @JoinColumn(name = "expense_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> images = new ArrayList<>();
}

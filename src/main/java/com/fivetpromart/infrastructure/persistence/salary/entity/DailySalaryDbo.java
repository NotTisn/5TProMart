package com.fivetpromart.infrastructure.persistence.salary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "daily_salaries", 
       indexes = {
           @Index(name = "idx_user_date", columnList = "user_id, date"),
           @Index(name = "idx_date", columnList = "date")
       })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySalaryDbo {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "work_hours", nullable = false)
    private Double workHours;

    @Column(name = "daily_salary", nullable = false, precision = 15, scale = 2)
    private BigDecimal dailySalary;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}

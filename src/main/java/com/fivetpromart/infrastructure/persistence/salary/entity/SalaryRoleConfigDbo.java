package com.fivetpromart.infrastructure.persistence.salary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "salary_role_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaryRoleConfigDbo {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "role", nullable = false, unique = true, length = 50)
    private String role;

    @Column(name = "hourly_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}

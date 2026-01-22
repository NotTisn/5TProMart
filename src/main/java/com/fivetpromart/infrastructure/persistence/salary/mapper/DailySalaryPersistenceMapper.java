package com.fivetpromart.infrastructure.persistence.salary.mapper;

import com.fivetpromart.domain.model.salary.DailySalary;
import com.fivetpromart.infrastructure.persistence.salary.entity.DailySalaryDbo;
import org.springframework.stereotype.Component;

@Component
public class DailySalaryPersistenceMapper {
    
    public DailySalaryDbo toDbo(DailySalary dailySalary) {
        return DailySalaryDbo.builder()
                .id(dailySalary.getId())
                .userId(dailySalary.getUserId())
                .date(dailySalary.getDate())
                .role(dailySalary.getRole())
                .hourlyRate(dailySalary.getHourlyRate())
                .workHours(dailySalary.getWorkHours())
                .dailySalary(dailySalary.getDailySalary())
                .createdAt(dailySalary.getCreatedAt())
                .build();
    }
    
    public DailySalary toDomain(DailySalaryDbo dbo) {
        return DailySalary.reconstitute(
                dbo.getId(),
                dbo.getUserId(),
                dbo.getDate(),
                dbo.getRole(),
                dbo.getHourlyRate(),
                dbo.getWorkHours(),
                dbo.getDailySalary(),
                dbo.getCreatedAt()
        );
    }
}

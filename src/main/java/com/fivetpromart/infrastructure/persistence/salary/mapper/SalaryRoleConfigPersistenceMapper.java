package com.fivetpromart.infrastructure.persistence.salary.mapper;

import com.fivetpromart.domain.model.salary.SalaryRoleConfig;
import com.fivetpromart.infrastructure.persistence.salary.entity.SalaryRoleConfigDbo;
import org.springframework.stereotype.Component;

@Component
public class SalaryRoleConfigPersistenceMapper {
    
    public SalaryRoleConfigDbo toDbo(SalaryRoleConfig config) {
        return SalaryRoleConfigDbo.builder()
                .id(config.getId())
                .role(config.getRole())
                .hourlyRate(config.getHourlyRate())
                .updatedAt(config.getUpdatedAt())
                .build();
    }
    
    public SalaryRoleConfig toDomain(SalaryRoleConfigDbo dbo) {
        return SalaryRoleConfig.reconstitute(
                dbo.getId(),
                dbo.getRole(),
                dbo.getHourlyRate(),
                dbo.getUpdatedAt()
        );
    }
}

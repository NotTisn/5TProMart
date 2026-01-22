package com.fivetpromart.infrastructure.persistence.salary;

import com.fivetpromart.application.port.out.ISalaryRoleConfigRepository;
import com.fivetpromart.domain.model.salary.SalaryRoleConfig;
import com.fivetpromart.infrastructure.persistence.salary.entity.SalaryRoleConfigDbo;
import com.fivetpromart.infrastructure.persistence.salary.mapper.SalaryRoleConfigPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.salary.repository.ISalaryRoleConfigJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalaryRoleConfigRepositoryAdapter implements ISalaryRoleConfigRepository {

    private final ISalaryRoleConfigJpaRepository jpaRepository;
    private final SalaryRoleConfigPersistenceMapper mapper;

    @Override
    public List<SalaryRoleConfig> findAll() {
        log.debug("Finding all salary role configs");
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SalaryRoleConfig> findByRole(String role) {
        log.debug("Finding salary config by role: {}", role);
        return jpaRepository.findByRole(role)
                .map(mapper::toDomain);
    }

    @Override
    public SalaryRoleConfig save(SalaryRoleConfig config) {
        log.debug("Saving salary config for role: {}", config.getRole());
        SalaryRoleConfigDbo dbo = mapper.toDbo(config);
        SalaryRoleConfigDbo saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }

    @Override
    public List<SalaryRoleConfig> saveAll(List<SalaryRoleConfig> configs) {
        log.debug("Saving {} salary configs", configs.size());
        List<SalaryRoleConfigDbo> dbos = configs.stream()
                .map(mapper::toDbo)
                .collect(Collectors.toList());
        
        List<SalaryRoleConfigDbo> saved = jpaRepository.saveAll(dbos);
        
        return saved.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}

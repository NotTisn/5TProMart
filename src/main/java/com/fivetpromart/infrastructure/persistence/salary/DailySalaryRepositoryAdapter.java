package com.fivetpromart.infrastructure.persistence.salary;

import com.fivetpromart.application.port.out.IDailySalaryRepository;
import com.fivetpromart.domain.model.salary.DailySalary;
import com.fivetpromart.infrastructure.persistence.salary.entity.DailySalaryDbo;
import com.fivetpromart.infrastructure.persistence.salary.mapper.DailySalaryPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.salary.repository.IDailySalaryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailySalaryRepositoryAdapter implements IDailySalaryRepository {

    private final IDailySalaryJpaRepository jpaRepository;
    private final DailySalaryPersistenceMapper mapper;

    @Override
    public DailySalary save(DailySalary dailySalary) {
        log.debug("Saving daily salary for user {} on date {}", dailySalary.getUserId(), dailySalary.getDate());
        DailySalaryDbo dbo = mapper.toDbo(dailySalary);
        DailySalaryDbo saved = jpaRepository.save(dbo);
        return mapper.toDomain(saved);
    }

    @Override
    public List<DailySalary> saveAll(List<DailySalary> dailySalaries) {
        log.debug("Saving {} daily salary records", dailySalaries.size());
        List<DailySalaryDbo> dbos = dailySalaries.stream()
                .map(mapper::toDbo)
                .collect(Collectors.toList());
        
        List<DailySalaryDbo> saved = jpaRepository.saveAll(dbos);
        
        return saved.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<DailySalary> findByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Finding daily salaries from {} to {}", startDate, endDate);
        return jpaRepository.findByDateRange(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<DailySalary> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        log.debug("Finding daily salaries for user {} from {} to {}", userId, startDate, endDate);
        return jpaRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByDate(LocalDate date) {
        return jpaRepository.existsByDate(date);
    }

    @Override
    public boolean existsByUserIdAndDate(String userId, LocalDate date) {
        return jpaRepository.existsByUserIdAndDate(userId, date);
    }
}

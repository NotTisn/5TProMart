package com.fivetpromart.infrastructure.persistence.staff.adapter;

import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.domain.model.Staff;
import com.fivetpromart.infrastructure.persistence.staff.StaffDbo;
import com.fivetpromart.infrastructure.persistence.staff.mapper.StaffPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.staff.repository.IStaffJpaRepository;
import com.fivetpromart.infrastructure.persistence.staff.spec.StaffSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StaffAdapter implements IStaffRepository {

    private final IStaffJpaRepository staffJpaRepository;
    private final StaffPersistenceMapper mapper;

    @Override
    public Staff save(Staff staff) {
        StaffDbo dbo = mapper.toDbo(staff);
        StaffDbo savedDbo = staffJpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public Optional<Staff> findById(String staffId) {
        return staffJpaRepository.findById(staffId).map(mapper::toDomain);
    }

    @Override
    public Optional<Staff> findByUsername(String username) {
        return staffJpaRepository.findByUsername(username).map(mapper::toDomain);
    }

    @Override
    public Optional<Staff> findByEmail(String email) {
        return staffJpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(String staffId) {
        return staffJpaRepository.existsById(staffId);
    }

    @Override
    public boolean existsByUsername(String username) {
        return staffJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return staffJpaRepository.existsByEmail(email);
    }

    @Override
    public void deleteById(String staffId) {
        staffJpaRepository.deleteById(staffId);
    }

    @Override
    public Page<Staff> searchStaff(StaffSearchQuery query, Pageable pageable) {
        Specification<StaffDbo> spec = StaffSpecification.getSpec(query);
        Page<StaffDbo> dboPage = staffJpaRepository.findAll(spec, pageable);
        return dboPage.map(mapper::toDomain);
    }
}

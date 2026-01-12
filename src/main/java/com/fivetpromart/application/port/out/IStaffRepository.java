package com.fivetpromart.application.port.out;

import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.domain.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IStaffRepository {
    Staff save(Staff staff);
    Optional<Staff> findById(String staffId);
    Optional<Staff> findByUsername(String username);
    Optional<Staff> findByEmail(String email);
    boolean existsById(String staffId);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteById(String staffId);
    Page<Staff> searchStaff(StaffSearchQuery query, Pageable pageable);
}

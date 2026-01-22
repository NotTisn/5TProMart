package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.application.dto.query.StaffSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IStaffUseCasePort {
    StaffAccountDto createStaffAccount(StaffCreationCommand command);
    StaffAccountDto updateStaffAccount(String staffId, StaffUpdateCommand command);
    StaffAccountDto getStaffById(String staffId);
    void deleteStaffById(String staffId);
    Page<StaffAccountDto> getAllStaff(StaffSearchQuery query, Pageable pageable);
    StaffAccountDto restoreStaff(String staffId);
}

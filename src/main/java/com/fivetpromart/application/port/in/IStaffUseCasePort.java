package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;

public interface IStaffUseCasePort {
    StaffAccountDto createStaffAccount(StaffCreationCommand command);

}

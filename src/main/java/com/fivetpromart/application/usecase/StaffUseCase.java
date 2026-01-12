package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.application.mapper.StaffDataMapper;
import com.fivetpromart.application.port.in.IStaffUseCasePort;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffUseCase implements IStaffUseCasePort {
    
    // TODO: Inject dependencies
    private final IStaffRepository staffRepository;
    private final StaffDataMapper mapper;
    private final IdentityProviderPort identityProviderPort;
    
    @Override
    public StaffAccountDto createStaffAccount(StaffCreationCommand command) {
        // TODO: Implement staff account creation logic
        // TODO: 1. Validate username and email uniqueness
        // TODO: 2. Create user in Keycloak with username, password, email
        // TODO: 3. Get userId from Keycloak
        // TODO: 4. Create Staff domain model with factory method
        // TODO:    Staff.create(userId, username, fullName, email, phoneNumber, accountType, dateOfBirth, location, bio)
        // TODO: 5. Save staff profile to repository
        // TODO: 6. Map to StaffAccountDto and return
        // TODO: 7. Handle exceptions: UsernameAlreadyExistsException, EmailAlreadyExistsException
        return null;
    }
    
    @Override
    public StaffAccountDto updateStaffAccount(String staffId, StaffUpdateCommand command) {
        // TODO: Implement staff account update logic
        // TODO: 1. Find existing staff by staffId (profileId)
        // TODO: 2. Throw StaffNotFoundException if not found
        // TODO: 3. Update staff domain model with new values
        // TODO:    staff.updateInfo(fullName, email, phoneNumber, accountType, dateOfBirth, location, bio)
        // TODO: 4. Validate email uniqueness if changed
        // TODO: 5. Update user in Keycloak if email changed
        // TODO: 6. Save updated staff to repository
        // TODO: 7. Map to StaffAccountDto and return
        return null;
    }
    
    @Override
    public StaffAccountDto getStaffById(String staffId) {
        // TODO: Implement get staff by ID
        // TODO: 1. Find staff in repository by staffId (profileId)
        // TODO: 2. Throw StaffNotFoundException if not found
        // TODO: 3. Map to StaffAccountDto and return
        return null;
    }
    
    @Override
    public void deleteStaffById(String staffId) {
        // TODO: Implement delete staff account
        // TODO: 1. Check if staff exists
        // TODO: 2. Throw StaffNotFoundException if not found
        // TODO: 3. Validate business rules:
        // TODO:    - Check if staff has pending/draft orders (import or selling orders)
        // TODO:    - If yes, throw StaffHasActiveOrdersException (HTTP 409)
        // TODO:    - Message: "This staff is currently import/selling orders."
        // TODO: 4. Delete user from Keycloak
        // TODO: 5. Delete staff profile from repository
    }
    
    @Override
    public Page<StaffAccountDto> getAllStaff(StaffSearchQuery query, Pageable pageable) {
        // TODO: Implement get all staff with search and pagination
        // TODO: 1. Build search specification from query
        // TODO:    - Search in: fullName, phoneNumber, userId
        // TODO:    - Filter by: accountType
        // TODO: 2. Call repository.searchStaff(query, pageable)
        // TODO: 3. Map Page<Staff> to Page<StaffAccountDto>
        // TODO: 4. Return paginated results
        return null;
    }
}

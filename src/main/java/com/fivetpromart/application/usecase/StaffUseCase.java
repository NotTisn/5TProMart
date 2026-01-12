package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.application.mapper.StaffDataMapper;
import com.fivetpromart.application.port.in.IStaffUseCasePort;
import com.fivetpromart.application.port.out.IStaffRepository;
import com.fivetpromart.application.port.out.IdentityProviderPort;
import com.fivetpromart.domain.exception.StaffAlreadyExistsException;
import com.fivetpromart.domain.model.PendingRegistration;
import com.fivetpromart.domain.model.Profile;
import com.fivetpromart.domain.model.Staff;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffUseCase implements IStaffUseCasePort {

    // Inject dependencies
    private final IStaffRepository staffRepository;
    private final StaffDataMapper mapper;
    private final IdentityProviderPort identityProviderPort;

    @Override
    public StaffAccountDto createStaffAccount(StaffCreationCommand command) {
        log.info("Creating staff account for username: {}", command.getUsername());

        // 1. Validate username and email uniqueness
        if (staffRepository.existsByUsername(command.getUsername())) {
            throw new StaffAlreadyExistsException("Username already exists: " + command.getUsername());
        }
        if (staffRepository.existsByEmail(command.getEmail())) {
            throw new StaffAlreadyExistsException("Email already exists: " + command.getEmail());
        }

        // 2. Create user in Keycloak with username, password, email
        String keycloakUserId = null;

        try {
            // A. Create User in Keycloak
            // Use raw password (not hashed) because Keycloak needs the original password to create user
            keycloakUserId = identityProviderPort.createUser(
                    command.getUsername(),
                    command.getEmail(),
                    command.getPassword()
            );
            log.info("Keycloak User created: {}", keycloakUserId);

            // B. Create Staff Profile in Local DB (with detailed mapping)
            createAndSaveLocalStaff(command, keycloakUserId);

            log.info("Staff account created successfully for user: {}", keycloakUserId);

            // C. Retrieve and return the created staff
            Staff createdStaff = staffRepository.findByUserId(keycloakUserId)
                    .orElseThrow(() -> new EntityNotFoundException("Staff not found after creation"));

            return mapper.toDto(createdStaff);

        } catch (StaffAlreadyExistsException e) {
            // Re-throw domain exceptions
            throw e;
        } catch (Exception e) {
            log.error("Staff creation failed. Rolling back...", e);

            // --- COMPENSATING TRANSACTION (MANUAL ROLLBACK) ---
            if (keycloakUserId != null) {
                try {
                    identityProviderPort.deleteUser(keycloakUserId);
                    log.warn("Rolled back user in Keycloak: {}", keycloakUserId);
                } catch (Exception rollbackEx) {
                    log.error("CRITICAL: Failed to rollback Keycloak user: {}", keycloakUserId, rollbackEx);
                    // TODO: Push to Dead Letter Queue for later processing
                }
            }

            throw new RuntimeException("Failed to create staff account", e);
        }
    }

    @Override
    public StaffAccountDto updateStaffAccount(String staffId, StaffUpdateCommand command) {
//        log.info("Updating staff account: {}", staffId);
//
//        // 1. Find existing staff by staffId (profileId)
//        Staff staff = staffRepository.findById(staffId)
//                .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + staffId));
//
//        // 2. Validate email uniqueness if changed
//        if (command.getEmail() != null && !command.getEmail().equals(staff.getEmail())) {
//            if (staffRepository.existsByEmail(command.getEmail())) {
//                throw new StaffAlreadyExistsException("Email already exists: " + command.getEmail());
//            }
//        }
//
//        // 4. Update staff domain model with new values
//        staff.updateInfo(
//                command.getFullName() != null ? command.getFullName() : staff.getFullName(),
//                command.getEmail() != null ? command.getEmail() : staff.getEmail(),
//                command.getPhoneNumber() != null ? command.getPhoneNumber() : staff.getPhoneNumber(),
//                command.getAccountType() != null ? command.getAccountType() : staff.getAccountType(),
//                command.getDateOfBirth() != null ? command.getDateOfBirth() : staff.getDateOfBirth(),
//                command.getLocation() != null ? command.getLocation() : staff.getLocation(),
//                command.getBio() != null ? command.getBio() : staff.getBio()
//        );
//
//        // 5. Save updated staff to repository
//        Staff updatedStaff = staffRepository.save(staff);
//
//        log.info("Staff account updated successfully: {}", staffId);
//
//        // 6. Map to StaffAccountDto and return
//        return mapper.toDto(updatedStaff);

        return null;
    }

    @Override
    public StaffAccountDto getStaffById(String staffId) {
        log.info("Getting staff by ID: {}", staffId);

        // 1. Find staff in repository by staffId (profileId)
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + staffId));

        // 2. Map to StaffAccountDto and return
        return mapper.toDto(staff);
    }

    @Override
    public void deleteStaffById(String staffId) {
//        log.info("Deleting staff account: {}", staffId);
//
//        // 1. Check if staff exists
//        Staff staff = staffRepository.findById(staffId)
//                .orElseThrow(() -> new EntityNotFoundException("Staff not found with ID: " + staffId));
//
//        // 2. Validate business rules
//        // TODO: Check if staff has pending/draft orders (import or selling orders)
//        // TODO: If yes, throw StaffHasActiveOrdersException (HTTP 409)
//        // TODO: Message: "This staff is currently handling import/selling orders."
//
//        // 3. Delete user from Keycloak
//        try {
//            identityProviderPort.deleteUser(staff.getUserId());
//            log.info("Deleted user from Keycloak: {}", staff.getUserId());
//        } catch (Exception e) {
//            log.error("Failed to delete user from Keycloak", e);
//            throw new RuntimeException("Failed to delete user from Keycloak", e);
//        }
//
//        // 4. Delete staff profile from repository
//        staffRepository.deleteById(staffId);
//
//        log.info("Staff account deleted successfully: {}", staffId);
    }

    @Override
    public Page<StaffAccountDto> getAllStaff(StaffSearchQuery query, Pageable pageable) {
//        log.info("Getting all staff with search query: {}", query);
//
//        // 1. Call repository.searchStaff with query and pagination
//        Page<Staff> staffPage = staffRepository.searchStaff(query, pageable);
//
//        // 2. Map Page<Staff> to Page<StaffAccountDto>
//        return staffPage.map(mapper::toDto);

        return null;
    }

    // --- Private Helpers ---

    /**
     * Create and save local staff profile in database
     * Uses granular domain methods for clear separation of concerns
     */
    private void createAndSaveLocalStaff(StaffCreationCommand command, String userId) {
        // 1. Create Staff domain model with core identity information
        Staff staff = Staff.create(
                userId,
                command.getUsername(),
                command.getFullName(),
                command.getEmail(),
                command.getPhoneNumber(),
                command.getAccountType(),
                command.getDateOfBirth(),
                command.getLocation(),
                command.getBio()
        );

        // 2. Save the staff profile to repository
        staffRepository.save(staff);

        log.info("Local staff profile created and saved: profileId={}, userId={}",
                staff.getProfileId(), userId);
    }
}

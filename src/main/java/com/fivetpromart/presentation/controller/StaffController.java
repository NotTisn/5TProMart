package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.StaffAccountDto;
import com.fivetpromart.application.dto.command.StaffCreationCommand;
import com.fivetpromart.application.dto.command.StaffUpdateCommand;
import com.fivetpromart.application.dto.query.StaffSearchQuery;
import com.fivetpromart.application.usecase.StaffUseCase;
import com.fivetpromart.presentation.dto.request.StaffRequest;
import com.fivetpromart.presentation.dto.request.StaffUpdateRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.PaginationMeta;
import com.fivetpromart.presentation.dto.response.StaffResponse;
import com.fivetpromart.presentation.mapper.StaffPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffUseCase staffUseCase;
    private final StaffPresentationMapper mapper;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<List<StaffResponse>> getAllStaff(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
            @PageableDefault(size = 10, sort = "fullName") Pageable pageable
    ) {
        StaffSearchQuery query = StaffSearchQuery.builder()
                .search(search)
                .accountType(accountType)
                .includeDeleted(includeDeleted)
                .build();

        Page<StaffAccountDto> pageResult = staffUseCase.getAllStaff(query, pageable);

        List<StaffResponse> responses = pageResult.getContent().stream()
                .map(mapper::toResponse)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .totalItems(pageResult.getTotalElements()) // Tổng số bản ghi
                .itemsPerPage(pageResult.getSize())        // Kích thước trang
                .totalPages(pageResult.getTotalPages())    // Tổng số trang
                .startPage(pageResult.getNumber() + 1)     // QUAN TRỌNG: Spring bắt đầu từ 0, bạn muốn 1 thì phải +1
                .build();

        return ApiResponse.<List<StaffResponse>>builder()
                .success(true)
                .statusCode(200)
                .message("Get staff list successfully.")
                .data(responses)
                .pagination(meta)
                .build();
    }

    @GetMapping("/{staffId}")
    @PreAuthorize("hasAnyRole('Admin', 'Manager')")
    public ApiResponse<StaffResponse> getStaffById(
            @PathVariable String staffId
    ) {
        StaffAccountDto dto = staffUseCase.getStaffById(staffId);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .message("Get staff detail successfully.")
                .data(mapper.toResponse(dto))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<StaffResponse> createStaff(
            @Valid @RequestBody StaffRequest request
    ) {
        StaffCreationCommand command = mapper.toCreateCommand(request);
        StaffAccountDto dto = staffUseCase.createStaffAccount(command);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Staff created successfully.")
                .data(mapper.toResponse(dto))
                .build();
    }

    @PutMapping("/{staffId}")
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<StaffResponse> updateStaff(
            @PathVariable String staffId,
            @Valid @RequestBody StaffUpdateRequest request
    ) {
        StaffUpdateCommand command = mapper.toUpdateCommand(request);
        StaffAccountDto dto = staffUseCase.updateStaffAccount(staffId, command);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Staff updated successfully.")
                .data(mapper.toResponse(dto))
                .build();
    }

    @DeleteMapping("/{staffId}")
    @PreAuthorize("hasRole('Admin')")
    public ApiResponse<Void> deleteStaff(
            @PathVariable String staffId
    ) {
        staffUseCase.deleteStaffById(staffId);

        return ApiResponse.<Void>builder()
                .success(true)
                .statusCode(204)
                .message("Staff deleted successfully.")
                .build();
    }

    @PostMapping("/{staffId}/restore")
    @PreAuthorize("hasRole('Admin')")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<StaffResponse> restoreStaff(
            @PathVariable String staffId
    ) {
        StaffAccountDto dto = staffUseCase.restoreStaff(staffId);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully restored staff")
                .data(mapper.toResponse(dto))
                .build();
    }
}

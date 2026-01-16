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

    // TODO: Add security annotation - only Admin can access
    // TODO: @PreAuthorize("hasRole('Admin')")
    
    @GetMapping
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<List<StaffResponse>> getAllStaff(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String accountType,
            @PageableDefault(size = 10, sort = "fullName") Pageable pageable
    ) {
        // TODO: Map query parameters to StaffSearchQuery
        StaffSearchQuery query = StaffSearchQuery.builder()
                .search(search)
                .accountType(accountType)
                .build();

        // TODO: Call use case
        Page<StaffAccountDto> pageResult = staffUseCase.getAllStaff(query, pageable);

        // TODO: Map to response
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
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<StaffResponse> getStaffById(
            @PathVariable String staffId
    ) {
        // TODO: Call use case
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
        // TODO: Map request to command
        StaffCreationCommand command = mapper.toCreateCommand(request);

        // TODO: Call use case
        StaffAccountDto dto = staffUseCase.createStaffAccount(command);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Staff created successfully.")
                .data(mapper.toResponse(dto))
                .build();
    }

    @PutMapping("/{staffId}")
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<StaffResponse> updateStaff(
            @PathVariable String staffId,
            @Valid @RequestBody StaffUpdateRequest request
    ) {
        // TODO: Map request to command
        StaffUpdateCommand command = mapper.toUpdateCommand(request);

        // TODO: Call use case
        StaffAccountDto dto = staffUseCase.updateStaffAccount(staffId, command);

        return ApiResponse.<StaffResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Staff updated successfully.")
                .data(mapper.toResponse(dto))
                .build();
    }

    @DeleteMapping("/{staffId}")
    //@PreAuthorize("hasRole('Admin')")
    public ApiResponse<Void> deleteStaff(
            @PathVariable String staffId
    ) {
        // TODO: Call use case
        staffUseCase.deleteStaffById(staffId);

        return ApiResponse.<Void>builder()
                .success(true)
                .statusCode(204)
                .message("Staff deleted successfully.")
                .build();
    }
}

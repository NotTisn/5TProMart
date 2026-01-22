package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.query.WorkScheduleSearchQuery;
import com.fivetpromart.application.usecase.WorkScheduleUseCase;
import com.fivetpromart.presentation.dto.request.AssignStaffRequest;
import com.fivetpromart.presentation.dto.request.RemoveStaffRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.WorkScheduleResponse;
import com.fivetpromart.presentation.mapper.WorkSchedulePresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {
    
    private final WorkScheduleUseCase useCase;
    private final WorkSchedulePresentationMapper mapper;
    
    @GetMapping
    public ApiResponse<List<WorkScheduleResponse>> getSchedules(
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam(required = false) String profileId,
            @RequestParam(required = false) String workShiftId
    ) {
        var query = WorkScheduleSearchQuery.builder()
                .startDate(startDate)
                .endDate(endDate)
                .profileId(profileId)
                .workShiftId(workShiftId)
                .build();
        
        var dtos = useCase.getSchedules(query);
        var responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        
        return ApiResponse.<List<WorkScheduleResponse>>builder()
                .success(true)
                .message("Get schedules successfully.")
                .data(responses)
                .build();
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> assignStaffToShift(
            @Valid @RequestBody AssignStaffRequest request
    ) {
        try {
            var command = mapper.toAssignCommand(request);
            var result = useCase.assignStaffToShift(command);
            
            return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
                    .success(true)
                    .message("Staff assigned successfully.")
                    .data(result)
                    .build());
        } catch (WorkScheduleUseCase.AssignmentConflictException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.<Map<String, Object>>builder()
                            .success(false)
                            .message("Assignment failed due to conflicts.")
                            .build());
        }
    }
    
    @DeleteMapping
    public ApiResponse<Map<String, Object>> removeStaff(
            @Valid @RequestBody RemoveStaffRequest request
    ) {
        var command = mapper.toRemoveCommand(request);
        var result = useCase.removeStaffFromShift(command);
        
        return ApiResponse.<Map<String, Object>>builder()
                .success(true)
                .message("Staff removed successfully.")
                .data(result)
                .build();
    }
}

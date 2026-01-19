package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.usecase.WorkShiftUseCase;
import com.fivetpromart.presentation.dto.request.CreateWorkShiftRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CreateWorkShiftResponse;
import com.fivetpromart.presentation.dto.response.WorkShiftResponse;
import com.fivetpromart.presentation.mapper.WorkShiftPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WorkShiftController {

    private final WorkShiftUseCase useCase;
    private final WorkShiftPresentationMapper mapper;

    @GetMapping("/work-shifts")
    public ApiResponse<List<WorkShiftResponse>> getWorkShifts(
            @RequestParam(required = false) Boolean isActive
    ) {
        var dtos = useCase.getWorkShifts(isActive);
        var responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<WorkShiftResponse>>builder()
                .success(true)
                .message("Get work shifts successfully.")
                .data(responses)
                .build();
    }

    @PostMapping("/work-shift-templates")
    @ResponseStatus(HttpStatus.CREATED) // Explicitly set 201 Created
    public ApiResponse<CreateWorkShiftResponse> createWorkShift(
            @Valid @RequestBody CreateWorkShiftRequest request
    ) {
        // No try-catch here! Let GlobalExceptionHandler handle exceptions.
        var command = mapper.toCommand(request);
        var dto = useCase.createWorkShift(command);
        var response = mapper.toCreateResponse(dto);

        return ApiResponse.<CreateWorkShiftResponse>builder()
                .success(true)
                .message("Shift template created successfully.")
                .data(response)
                .build();
    }
}
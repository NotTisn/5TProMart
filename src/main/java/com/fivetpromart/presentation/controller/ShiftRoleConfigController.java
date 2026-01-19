package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.usecase.ShiftRoleConfigUseCase;
import com.fivetpromart.presentation.dto.request.CreateRoleConfigRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CreateRoleConfigResponse;
import com.fivetpromart.presentation.dto.response.ShiftRoleConfigResponse;
import com.fivetpromart.presentation.mapper.ShiftRoleConfigPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/shift-role-configs")
@RequiredArgsConstructor
public class ShiftRoleConfigController {

    private final ShiftRoleConfigUseCase useCase;
    private final ShiftRoleConfigPresentationMapper mapper;

    @GetMapping
    public ApiResponse<List<ShiftRoleConfigResponse>> getRoleConfigs(
            @RequestParam(required = false) Boolean isActive
    ) {
        var dtos = useCase.getRoleConfigs(isActive);
        var responses = dtos.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ApiResponse.<List<ShiftRoleConfigResponse>>builder()
                .success(true)
                .message("Get role configs successfully.")
                .data(responses)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CreateRoleConfigResponse> createRoleConfig(
            @Valid @RequestBody CreateRoleConfigRequest request
    ) {
        var command = mapper.toCommand(request);
        var dto = useCase.createRoleConfig(command);
        var response = mapper.toCreateResponse(dto);

        return ApiResponse.<CreateRoleConfigResponse>builder()
                .success(true)
                .message("Role config created successfully.")
                .data(response)
                .build();
    }
}
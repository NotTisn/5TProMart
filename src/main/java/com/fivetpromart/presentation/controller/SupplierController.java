package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.application.usecase.SupplierUseCase;
import com.fivetpromart.infrastructure.persistence.supplier.mapper.SupplierPersistenceMapper;
import com.fivetpromart.presentation.dto.request.SupplierRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import com.fivetpromart.presentation.dto.response.SupplierResponse;
import com.fivetpromart.presentation.mapper.SupplierPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierUseCase supplierUseCase;
    private final SupplierPresentationMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SupplierResponse> addNewSupplier (
            @Valid @RequestBody SupplierRequest request
    ) {
        SupplierCreationCommand command = mapper.toCreateCommand(request);
        SupplierDto dto = supplierUseCase.createSupplier(command);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.CREATED.value())
                .message("Successfully added new supplier")
                .data(mapper.toResponse(dto))
                .build();
    }

    @PutMapping("/{supplierId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<SupplierResponse> updateSupplier (
            @PathVariable String supplierId,
            @Valid @RequestBody SupplierRequest request
    ) {
        SupplierUpdateCommand command = mapper.toUpdateCommand(request);
        command = command.toBuilder()
                    .supplierId(supplierId)
                    .build();

        SupplierDto dto = supplierUseCase.updateSupplier(command);

        return ApiResponse.<SupplierResponse>builder()
                .success(true)
                .statusCode(HttpStatus.OK.value())
                .message("Successfully update supplier")
                .data(mapper.toResponse(dto))
                .build();
    }
}

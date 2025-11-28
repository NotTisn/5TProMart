package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.command.CustomerCommand;
import com.fivetpromart.application.port.in.ICustomerUseCasePort;
import com.fivetpromart.presentation.dto.request.CustomerInitRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import com.fivetpromart.presentation.mapper.CustomerPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerUseCasePort customerUseCase;
    private final CustomerPresentationMapper mapper;

    @PostMapping()
    public ApiResponse<CustomerResponse> addNewCustomer(
            @Valid @RequestBody CustomerInitRequest request
    ) {
        CustomerCommand appDto = mapper.toDto(request);
        CustomerResponse appResponse = mapper.toResponse(customerUseCase.addNewCustomer(appDto));

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer added successfully")
                .data(appResponse)
                .build();
    }
}

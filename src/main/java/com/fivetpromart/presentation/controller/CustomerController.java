package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.application.port.in.ICustomerUseCasePort;
import com.fivetpromart.presentation.dto.request.CustomerRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import com.fivetpromart.presentation.mapper.CustomerPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerUseCasePort customerUseCase;
    private final CustomerPresentationMapper mapper;

    @PostMapping()
    public ApiResponse<CustomerResponse> addNewCustomer(
            @Valid @RequestBody CustomerRequest request
    ) {
        CustomerCreationCommand appDto = mapper.toDto(request);
        CustomerResponse appResponse = mapper.toResponse(customerUseCase.addNewCustomer(appDto));

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer added successfully")
                .data(appResponse)
                .build();
    }

    @PatchMapping("/{customerId}") // 1. Quan trọng: Khai báo biến đường dẫn
    public ApiResponse<CustomerResponse> updateCustomer(
            @PathVariable String customerId, // 2. Hứng biến từ URL
            @Valid @RequestBody CustomerRequest request // 3. Hứng dữ liệu từ Body
    ) {
        // Bước 1: Map dữ liệu từ Body sang Command
        CustomerUpdateCommand command = mapper.toUpdateDto(request);

        // Bước 2: "Tiêm" ID từ URL vào Command (Merge)
        // Lưu ý: Class CustomerUpdateCommand phải có @Builder(toBuilder = true)
        command = command.toBuilder()
                .customerId(customerId)
                .build();

        // Bước 3: Gọi UseCase
        var resultDto = customerUseCase.updateCustomer(command);

        // Bước 4: Map kết quả trả về
        CustomerResponse response = mapper.toResponse(resultDto);

        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Customer updated successfully")
                .data(response)
                .build();
    }
}

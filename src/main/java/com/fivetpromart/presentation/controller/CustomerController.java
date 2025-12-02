package com.fivetpromart.presentation.controller;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.application.port.in.ICustomerUseCasePort;
import com.fivetpromart.presentation.dto.request.CustomerRequest;
import com.fivetpromart.presentation.dto.response.ApiResponse;
import com.fivetpromart.presentation.dto.response.CustomerResponse;
import com.fivetpromart.presentation.dto.response.PaginationMeta;
import com.fivetpromart.presentation.dto.response.ProductResponse;
import com.fivetpromart.presentation.mapper.CustomerPresentationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        CustomerCreationCommand appDto = mapper.toCommand(request);
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
        CustomerUpdateCommand command = mapper.toUpdateDomain(request);

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

    @DeleteMapping("/{customerId}")
    public ApiResponse deleteCustomer(
            @PathVariable String customerId
    ) {
        customerUseCase.deleteCustomer(customerId);
        return ApiResponse.builder()
                .success(true)
                .message(null)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<CustomerResponse>> getAllCustomers() {
        List<CustomerDto> customerDtos = customerUseCase.getAllCustomers();

        // Dùng Stream để map
        List<CustomerResponse> responses = customerDtos.stream()
                .map(mapper::toResponse)
                .toList();

        return ApiResponse.<List<CustomerResponse>>builder()
                .success(true)
                .data(responses)
                .build();
    }

    @GetMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<CustomerResponse> getCustomerById(
            @PathVariable String customerId
    ) {
        CustomerDto customerDto = customerUseCase.getCustomerById(customerId);
        CustomerResponse customerResponse = mapper.toResponse(customerDto);
        return ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Successfully retrieved a customer")
                .data(customerResponse)
                .build();
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<CustomerResponse>> getAllCustomersByPage(
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String customerId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        CustomerSearchQuery query =  CustomerSearchQuery.builder()
                .customerName(customerName)
                .customerId(customerId)
                .build();
        Page<CustomerDto> pageResult = customerUseCase.getAllCustomers(query, pageable);

        List<CustomerResponse> responses = pageResult.stream()
                .map(mapper::toResponse)
                .toList();

        PaginationMeta meta = PaginationMeta.builder()
                .totalItems(pageResult.getTotalElements()) // Tổng số bản ghi
                .itemsPerPage(pageResult.getSize())        // Kích thước trang
                .totalPages(pageResult.getTotalPages())    // Tổng số trang
                .startPage(pageResult.getNumber() + 1)     // QUAN TRỌNG: Spring bắt đầu từ 0, bạn muốn 1 thì phải +1
                .build();

        // 4. Trả về kết quả gộp
        return ApiResponse.<List<CustomerResponse>>builder()
                .success(true)
                .statusCode(200)
                .message("Get products successfully")
                .data(responses)  // Mảng dữ liệu
                .pagination(meta)    // Thông tin phân trang
                .build();
    }
}

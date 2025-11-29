package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.application.mapper.CustomerDataMapper;
import com.fivetpromart.application.port.in.ICustomerUseCasePort;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerUseCase implements ICustomerUseCasePort {

    private final ICustomerRepository customerRepository;
    private final CustomerDataMapper mapper;

    @Override
    @Transactional
    public CustomerDto addNewCustomer(CustomerCreationCommand command) {

        if (customerRepository.existsByPhoneNumber(command.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        Customer newCustomer = Customer.create(
                command.getFullName(),
                command.getPhoneNumber(),
                command.getGender(),
                command.getDateOfBirth()
        );

        Customer savedCustomer = customerRepository.save(newCustomer);

        return mapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerDto updateCustomer(CustomerUpdateCommand command) {
        // 1. LOAD: Tìm khách hàng cần sửa (Bắt buộc phải tìm bằng ID)
        // Giả sử CustomerCreationCommand có trường customerId
        Customer customer = customerRepository.findById(command.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // 2. VALIDATE: Kiểm tra trùng số điện thoại
        // Logic: Nếu User đổi sang SĐT mới, và SĐT đó đã có người KHÁC dùng -> Lỗi
        if (!customer.getPhoneNumber().equals(command.getPhoneNumber())
                && customerRepository.existsByPhoneNumber(command.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        // 3. MUTATE: Gọi Business Method của Domain (Rich Model)
        // Thay vì dùng setter, ta dùng hành động cụ thể đã định nghĩa trong Customer.java

        // Cập nhật thông tin cá nhân
        customer.updateProfile(
                command.getFullName(),
                command.getGender(),
                command.getDateOfBirth()
        );

        // Cập nhật số điện thoại (nếu thay đổi)
        if (!customer.getPhoneNumber().equals(command.getPhoneNumber())) {
            customer.changePhoneNumber(command.getPhoneNumber());
        }

        // 4. PERSIST: Lưu xuống DB
        // JPA thông minh sẽ tự biết đây là Update vì entity đã có ID
        Customer updatedCustomer = customerRepository.save(customer);

        // 5. MAP: Trả về kết quả
        return mapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_EXISTED));

        customerRepository.delete(customer);
    }
}

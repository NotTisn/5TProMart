package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCommand;
import com.fivetpromart.application.mapper.CustomerDataMapper;
import com.fivetpromart.application.port.in.ICustomerUseCasePort;
import com.fivetpromart.application.port.out.ICustomerRepository;
import com.fivetpromart.domain.model.Customer;
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
    public CustomerDto addNewCustomer(CustomerCommand command) {

        if (customerRepository.existsByPhoneNumber(command.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists: " + command.getPhoneNumber());
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
}

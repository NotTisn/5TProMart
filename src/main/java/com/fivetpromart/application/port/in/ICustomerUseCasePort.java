package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerUseCasePort {
    CustomerDto addNewCustomer(CustomerCreationCommand command);
    CustomerDto updateCustomer(CustomerUpdateCommand command);
    void deleteCustomer(String customerId);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(String customerId);
    Page<CustomerDto> getAllCustomers(CustomerSearchQuery query, Pageable pageable);

}

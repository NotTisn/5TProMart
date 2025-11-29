package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;

import java.util.List;

public interface ICustomerUseCasePort {
    CustomerDto addNewCustomer(CustomerCreationCommand command);
    CustomerDto updateCustomer(CustomerUpdateCommand command);
    void deleteCustomer(String customerId);
    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(String customerId);
}

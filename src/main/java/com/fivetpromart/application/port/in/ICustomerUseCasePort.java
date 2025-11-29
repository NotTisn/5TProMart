package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCreationCommand;
import com.fivetpromart.application.dto.command.CustomerUpdateCommand;

public interface ICustomerUseCasePort {
    CustomerDto addNewCustomer(CustomerCreationCommand command);
    CustomerDto updateCustomer(CustomerUpdateCommand command);
}

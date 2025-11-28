package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.command.CustomerCommand;

public interface ICustomerUseCasePort {
    CustomerDto addNewCustomer(CustomerCommand command);
}

package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;

public interface ISupplierUseCasePort {
    SupplierDto createSupplier(SupplierCreationCommand command);
    SupplierDto updateSupplier(SupplierUpdateCommand command);
    SupplierDto getSupplierById(String supplierId);
    void deleteSupplierById(String supplierId);
}

package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CustomerDto;
import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.application.dto.query.CustomerSearchQuery;
import com.fivetpromart.application.dto.query.SupplierSearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISupplierUseCasePort {
    SupplierDto createSupplier(SupplierCreationCommand command);
    SupplierDto updateSupplier(SupplierUpdateCommand command);
    SupplierDto getSupplierById(String supplierId);
    void deleteSupplierById(String supplierId);
    Page<SupplierDto> getAllSuppliers(SupplierSearchQuery query, Pageable pageable);

}

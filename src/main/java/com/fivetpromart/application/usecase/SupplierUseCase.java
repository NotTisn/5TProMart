package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.SupplierDto;
import com.fivetpromart.application.dto.command.SupplierCreationCommand;
import com.fivetpromart.application.dto.command.SupplierUpdateCommand;
import com.fivetpromart.application.mapper.SupplierDataMapper;
import com.fivetpromart.application.port.in.ISupplierUseCasePort;
import com.fivetpromart.application.port.out.ISupplierRepository;
import com.fivetpromart.domain.exception.SupplierNotFoundException;
import com.fivetpromart.domain.model.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierUseCase implements ISupplierUseCasePort {

    private final ISupplierRepository supplierRepository;
    private final SupplierDataMapper mapper;

    @Override
    public SupplierDto createSupplier(SupplierCreationCommand command) {

        Supplier supplier = Supplier.create(
                command.getSupplierName(),
                command.getSupplierType(),
                command.getPhoneNumber(),
                command.getAddress(),
                command.getSuppliedProductType()
        );

        Supplier savedSupplier = supplierRepository.save(supplier);
        return mapper.toDto(savedSupplier);
    }

    @Override
    public SupplierDto updateSupplier(SupplierUpdateCommand command) {
        Supplier supplier = supplierRepository.findById(command.getSupplierId())
                        .orElseThrow(() -> new SupplierNotFoundException(command.getSupplierId()));

        supplier.updateInfo(
                command.getSupplierName(),
                command.getSupplierType(),
                command.getPhoneNumber(),
                command.getAddress(),
                command.getSuppliedProductType()
        );

        return mapper.toDto(supplierRepository.save(supplier));
    }

    @Override
    public SupplierDto getSupplierById(String supplierId) {
        return supplierRepository.findById(supplierId).map(mapper::toDto)
                .orElseThrow(() -> new SupplierNotFoundException(supplierId));
    }

    @Override
    public void deleteSupplierById(String supplierId) {
        if(!supplierRepository.existsById(supplierId))
            throw new SupplierNotFoundException(supplierId);

        supplierRepository.deleteById(supplierId);
    }
}

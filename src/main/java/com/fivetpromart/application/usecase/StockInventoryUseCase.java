package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.mapper.StockInventoryDataMapper;
import com.fivetpromart.application.port.in.IStockInventoryUseCasePort;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.NegativeValueException;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.StockInventory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInventoryUseCase implements IStockInventoryUseCasePort {

    private final IStockInventoryRepository stockInventoryRepository;
    private final IProductRepository productRepository;
    private final StockInventoryDataMapper mapper;

//    @Override
//    @Transactional(readOnly = true)
//    public List<StockInventoryDto> searchStockInventories(StockInventorySearchQuery query) {
//        log.info("Searching stock inventories with query: {}", query);
//
//        List<StockInventory> inventories = stockInventoryRepository.searchStockInventories(query);
//
//        return inventories.stream()
//                .map(mapper::toDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public StockInventoryDto getStockInventoryById(String lotId) {
//        log.info("Getting stock inventory by ID: {}", lotId);
//
//        StockInventory inventory = stockInventoryRepository.findById(lotId)
//                .orElseThrow(() -> new EntityNotFoundException("Stock inventory not found with ID: " + lotId));
//
//        return mapper.toDto(inventory);
//    }

    @Override
    @Transactional
    public StockInventoryDto createStockInventory(StockInventoryCreationCommand command) {
        log.info("Creating stock inventory for productId: {}", command.getProductId());

        // Validate product exists
        Product product = productRepository.findById(command.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + command.getProductId()));

        // Validate stock quantity
        if (command.getStockQuantity() == null || command.getStockQuantity() <= 0) {
            throw new NegativeValueException("Stock quantity must be greater than 0");
        }

        // Create domain model
        StockInventory inventory = StockInventory.create(
                command.getProductId(),
                command.getManufactureDate(),
                command.getExpirationDate(),
                command.getStockQuantity(),
                command.getImportPrice(),
                command.getStatus() != null ? command.getStatus() : "GOOD"
        );

        // Save
        StockInventory savedInventory = stockInventoryRepository.save(inventory);

        log.info("Stock inventory created successfully: {}", savedInventory.getLotId());

        return mapper.toDto(savedInventory);
    }

    @Override
    @Transactional
    public StockInventoryDto updateStockInventory(String lotId, StockInventoryUpdateCommand command) {
        log.info("Updating stock inventory: {}", lotId);

        // Find existing inventory
        StockInventory inventory = stockInventoryRepository.findById(lotId)
                .orElseThrow(() -> new EntityNotFoundException("Stock inventory not found with ID: " + lotId));

        // Validate stock quantity if provided
        if (command.getStockQuantity() != null && command.getStockQuantity() <= 0) {
            throw new NegativeValueException("Stock quantity must be greater than 0");
        }

        // Update only stockQuantity and status (as per API spec)
        inventory.update(
                null,  // productId - not updatable
                null,  // manufactureDate - not updatable
                null,  // expirationDate - not updatable
                command.getStockQuantity() != null ? command.getStockQuantity() : inventory.getStockQuantity(),
                null,  // importPrice - not updatable
                command.getStatus() != null ? command.getStatus() : inventory.getStatus()
        );

        // Save
        StockInventory updatedInventory = stockInventoryRepository.save(inventory);

        log.info("Stock inventory updated successfully: {}", lotId);

        return mapper.toDto(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteById(String lotId) {
        log.info("Deleting stock inventory: {}", lotId);

        if (!stockInventoryRepository.existsById(lotId)) {
            throw new EntityNotFoundException("Stock inventory not found with ID: " + lotId);
        }

        stockInventoryRepository.deleteById(lotId);

        log.info("Stock inventory deleted successfully: {}", lotId);
    }
}

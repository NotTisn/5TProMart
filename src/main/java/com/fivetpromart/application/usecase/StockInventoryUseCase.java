package com.fivetpromart.application.usecase;

import com.fivetpromart.application.dto.DisposalBatchResultDto;
import com.fivetpromart.application.dto.DisposeLotResultDto;
import com.fivetpromart.application.dto.StockInventoryDto;
import com.fivetpromart.application.dto.command.DisposalBatchCommand;
import com.fivetpromart.application.dto.command.DisposeLotCommand;
import com.fivetpromart.application.dto.command.StockInventoryCreationCommand;
import com.fivetpromart.application.dto.command.StockInventoryUpdateCommand;
import com.fivetpromart.application.dto.query.StockInventorySearchQuery;
import com.fivetpromart.application.mapper.StockInventoryDataMapper;
import com.fivetpromart.application.port.in.IStockInventoryUseCasePort;
import com.fivetpromart.application.port.out.IProductRepository;
import com.fivetpromart.application.port.out.IStockInventoryRepository;
import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.InsufficientStockException;
import com.fivetpromart.domain.exception.LotNotFoundException;
import com.fivetpromart.domain.exception.NegativeValueException;
import com.fivetpromart.domain.model.Product;
import com.fivetpromart.domain.model.StockInventory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockInventoryUseCase implements IStockInventoryUseCasePort {

    private final IStockInventoryRepository stockInventoryRepository;
    private final IProductRepository productRepository;
    private final StockInventoryDataMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<StockInventoryDto> searchStockInventories(StockInventorySearchQuery query, Pageable pageable) {
        log.info("Searching stock inventories with query: {} and pageable: {}", query, pageable);
        
        Page<StockInventory> inventories = stockInventoryRepository.searchStockInventories(query, pageable);
        
        return inventories.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StockInventoryDto getStockInventoryById(String lotId) {
        log.info("Getting stock inventory by ID: {}", lotId);
        
        StockInventory inventory = stockInventoryRepository.findById(lotId)
                .orElseThrow(() -> new EntityNotFoundException("Stock inventory not found with ID: " + lotId));
        
        return mapper.toDto(inventory);
    }

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
                command.getImportPrice()
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
        
        String targetStatus = command.getStatus() != null ? command.getStatus() : inventory.getStatus();

        // 3. ÁP DỤNG LOGIC ƯU TIÊN HẾT HÀNG
        // Nếu newQuantity <= 0 thì finalStatus sẽ thành "OUT_OF_STOCK"
        // Nếu newQuantity > 0 thì finalStatus sẽ là targetStatus
        Long newQuantity = command.getStockQuantity() != null ? command.getStockQuantity() : inventory.getStockQuantity();
        String finalStatus = resolveFinalStatus(newQuantity, targetStatus);

        // Update only stockQuantity and status (as per API spec)
        inventory.update(
                null,  // productId - not updatable
                null,  // manufactureDate - not updatable
                null,  // expirationDate - not updatable
                newQuantity,
                null,  // importPrice - not updatable
                finalStatus
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

    @Override
    @Transactional
    public DisposeLotResultDto disposeLot(DisposeLotCommand command) {
        log.info("Disposing lot: {} with quantity: {}", command.getLotId(), command.getQuantity());

        // 1. Find lot
        StockInventory lot = stockInventoryRepository.findById(command.getLotId())
                .orElseThrow(() -> new LotNotFoundException(command.getLotId()));

        // 2. Validate quantity
        if (command.getQuantity() > lot.getStockQuantity()) {
            throw new InsufficientStockException(lot.getStockQuantity(), command.getQuantity());
        }
        
        
        // 3. Get product info
        Product product = productRepository.findById(lot.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + lot.getProductId()));

        // 1. Tính số lượng còn lại
        long remainingQuantity = lot.getStockQuantity() - command.getQuantity();
        
        // 2. ÁP DỤNG LOGIC ƯU TIÊN HẾT HÀNG
        // Status mong muốn là status hiện tại của lô (lot.getStatus())
        // Nếu remainingQuantity về 0 -> Hàm này sẽ trả về "OUT_OF_STOCK"
        // Nếu remainingQuantity > 0 -> Hàm này trả về lot.getStatus() (giữ nguyên)
        String finalStatus = resolveFinalStatus(remainingQuantity, lot.getStatus());
        
        lot.update(
                lot.getProductId(),
                lot.getManufactureDate(),
                lot.getExpirationDate(),
                remainingQuantity,
                lot.getImportPrice(),
                finalStatus
        );
        
        stockInventoryRepository.save(lot);
        
        stockInventoryRepository.save(lot);

        // 5. Calculate product total stock (across all lots)
        Long productTotalStock = stockInventoryRepository.getTotalStockByProductId(lot.getProductId());

        log.info("Lot {} disposed successfully, remaining: {}", command.getLotId(), remainingQuantity);

        // 6. Return result
        return DisposeLotResultDto.builder()
                .disposalId(UUID.randomUUID().toString())
                .lotId(lot.getLotId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .quantityDisposed(command.getQuantity())
                .remainingLotQuantity(remainingQuantity)
                .productTotalStock(productTotalStock != null ? productTotalStock : 0L)
                .disposedAt(LocalDateTime.now())
                .disposedBy(command.getStaffId())
                .reason(command.getReason())
                .notes(command.getNotes())
                .build();
    }

    @Override
    @Transactional
    public DisposalBatchResultDto createDisposalBatch(DisposalBatchCommand command) {
        log.info("Creating disposal batch with {} items", command.getItems().size());

        String disposalId = UUID.randomUUID().toString();
        long totalQuantity = 0;

        // Process each disposal item
        for (var item : command.getItems()) {
            // 1. Find lot
            StockInventory lot = stockInventoryRepository.findById(item.getLotId())
                    .orElseThrow(() -> new LotNotFoundException(item.getLotId()));

            // 2. Validate quantity
            if (item.getQuantity() > lot.getStockQuantity()) {
                throw new InsufficientStockException(lot.getStockQuantity(), item.getQuantity());
            }

            // 3. Deduct quantity
            long remainingQuantity = lot.getStockQuantity() - item.getQuantity();
            String finalStatus = resolveFinalStatus(remainingQuantity, lot.getStatus());

            lot.update(
                    lot.getProductId(),
                    lot.getManufactureDate(),
                    lot.getExpirationDate(),
                    remainingQuantity,
                    lot.getImportPrice(),
                    finalStatus
            );
            
            stockInventoryRepository.save(lot);
            
            // 4. Update product total stock
            Long productTotalStock = stockInventoryRepository.getTotalStockByProductId(lot.getProductId());
            Product product = productRepository.findById(lot.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + lot.getProductId()));
            // Note: You may need to update product.totalStockQuantity here if that field exists

            totalQuantity += item.getQuantity();
            
            log.info("Disposed {} items from lot {}, remaining: {}", item.getQuantity(), item.getLotId(), remainingQuantity);
        }

        // 5. Create disposal record (Note: This would typically be saved to a ProductDisposal collection)
        // For now, we just return the result
        
        log.info("Disposal batch {} created successfully with {} total items", disposalId, totalQuantity);

        return DisposalBatchResultDto.builder()
                .disposalId(disposalId)
                .staffId(command.getStaffId())
                .date(LocalDateTime.now())
                .totalItems(totalQuantity)
                .build();
    }
    /**
     * Helper: Tính toán trạng thái (Logic ưu tiên Quantity <= 0)
     * * Thứ tự ưu tiên (Tuyệt đối):
     * 1. Hết hàng (Quantity <= 0) -> "OUT_OF_STOCK" (Bất chấp hạn sử dụng)
     * 2. Đã hết hạn (Expiration < Now) -> "EXPIRED"
     * 3. Sắp hết hạn (Expiration <= Now + 7 days) -> "EXPIRING_SOON"
     * 4. Còn hàng -> "IN_STOCK"
     */
/**
     * Logic chốt trạng thái:
     * 1. Nếu quantity <= 0 -> BẮT BUỘC là "OUT_OF_STOCK"
     * 2. Ngược lại -> Giữ nguyên status mong muốn (proposedStatus)
     */
    private String resolveFinalStatus(Long quantity, String proposedStatus) {
        if (quantity != null && quantity <= 0) {
            return "OUT_OF_STOCK";
        }
        return proposedStatus;
    }
}


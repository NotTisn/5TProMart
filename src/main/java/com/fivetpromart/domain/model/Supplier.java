package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.NegativeValueException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {
    private String supplierId;
    private String supplierName;
    private String address;
    private String phoneNumber;
    private String representName;
    private String representPhoneNumber;
    private String supplierType;
    private List<SuppliedProduct> suppliedProducts;
    private BigDecimal currentDebt;

    // =================================================================
    // 1. FACTORY: TẠO MỚI
    // =================================================================
    public static Supplier create(
            String supplierName, 
            String address,
            String phoneNumber, 
            String representName,
            String representPhoneNumber,
            String supplierType, 
            List<String> suppliedProductIds
    ) {
        if (supplierName == null || supplierName.isBlank())
            throw new EmptyFieldException("Supplier name");
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new EmptyFieldException("Phone number");
        if (address == null || address.isBlank())
            throw new EmptyFieldException("Address");
        if(suppliedProductIds == null || suppliedProductIds.isEmpty())
            throw new EmptyFieldException("Supplied product type");
        if(supplierType == null || supplierType.isBlank())
            throw new EmptyFieldException("Supplier type");

        Supplier supplier = new Supplier();
        supplier.supplierId = UUID.randomUUID().toString();
        supplier.supplierName = supplierName;
        supplier.address = address;
        supplier.phoneNumber = phoneNumber;
        supplier.representName = representName;
        supplier.representPhoneNumber = representPhoneNumber;
        supplier.supplierType = supplierType;
        
        // Convert product IDs to SuppliedProduct objects with default values
        supplier.suppliedProducts = new ArrayList<>();
        for (String productId : suppliedProductIds) {
            supplier.suppliedProducts.add(SuppliedProduct.createNew(productId));
        }

        supplier.currentDebt = BigDecimal.ZERO;

        return supplier;
    }

    // =================================================================
    // 2. FACTORY: TÁI TẠO (Load từ DB)
    // =================================================================
    public static Supplier reconstitute(
            String supplierId,
            String supplierName,
            String address,
            String phoneNumber,
            String representName,
            String representPhoneNumber,
            String supplierType,
            List<SuppliedProduct> suppliedProducts,
            BigDecimal currentDebt
    ) {
        Supplier supplier = new Supplier();
        supplier.supplierId = supplierId;
        supplier.supplierName = supplierName;
        supplier.address = address;
        supplier.phoneNumber = phoneNumber;
        supplier.representName = representName;
        supplier.representPhoneNumber = representPhoneNumber;
        supplier.supplierType = supplierType;
        supplier.suppliedProducts = suppliedProducts != null ? suppliedProducts : new ArrayList<>();
        supplier.currentDebt = currentDebt;
        return supplier;
    }

    // =================================================================
    // 3. BUSINESS: CẬP NHẬT THÔNG TIN (Info)
    // Đổi thành void để đúng chuẩn State Mutation
    // =================================================================
    public void updateInfo(
            String supplierName, 
            String address,
            String phoneNumber, 
            String representName,
            String representPhoneNumber,
            String supplierType, 
            List<String> suppliedProductIds
    ) {
        if (supplierName != null && !supplierName.isBlank()) this.supplierName = supplierName;
        if (address != null && !address.isBlank()) this.address = address;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (representName != null) this.representName = representName;  // Allow empty for optional field
        if (representPhoneNumber != null) this.representPhoneNumber = representPhoneNumber;  // Allow empty for optional field
        if (supplierType != null && !supplierType.isBlank()) this.supplierType = supplierType;
        
        // Update supplied products
        if (suppliedProductIds != null && !suppliedProductIds.isEmpty()) {
            List<SuppliedProduct> newSuppliedProducts = new ArrayList<>();
            
            for (String productId : suppliedProductIds) {
                // Keep existing product info if already exists
                SuppliedProduct existing = this.suppliedProducts.stream()
                        .filter(sp -> sp.getProductId().equals(productId))
                        .findFirst()
                        .orElse(null);
                
                if (existing != null) {
                    newSuppliedProducts.add(existing);
                } else {
                    // New product - create with default values
                    newSuppliedProducts.add(SuppliedProduct.createNew(productId));
                }
            }
            
            this.suppliedProducts = newSuppliedProducts;
        }
    }

    // =================================================================
    // 4. BUSINESS: QUẢN LÝ NỢ (Debt) - Rich Domain Model
    // =================================================================

    /**
     * Nhập hàng -> Tăng nợ
     */
    public void recordPurchase(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeValueException("Purchase amount");
        }
        this.currentDebt = this.currentDebt.add(amount);
    }

    /**
     * Thanh toán tiền -> Giảm nợ
     */
    public void payDebt(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new NegativeValueException("Payment amount");
        }
        // Có thể cho phép nợ âm (trả dư) hoặc không, tùy logic
        this.currentDebt = this.currentDebt.subtract(amount);
    }
}
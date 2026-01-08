package com.fivetpromart.domain.model;

import com.fivetpromart.domain.exception.EmptyFieldException;
import com.fivetpromart.domain.exception.NegativeValueException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {
    private String supplierId;
    private String supplierName;
    private String supplierType;
    private String phoneNumber;
    private String address;
    private String suppliedProductType;
    private BigDecimal currentDebt;

    // =================================================================
    // 1. FACTORY: TẠO MỚI
    // =================================================================
    public static Supplier create(String supplierName, String supplierType, String phoneNumber, String address, String suppliedProductType) {
        if (supplierName == null || supplierName.isBlank())
            throw new EmptyFieldException("Supplier name");
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new EmptyFieldException("Phone number");
        if (address == null || address.isBlank())
            throw new EmptyFieldException("Address");
        if(suppliedProductType == null || suppliedProductType.isBlank())
            throw new EmptyFieldException("Supplied product type");
        if(supplierType == null || supplierType.isBlank())
            throw new EmptyFieldException("Supplier type");

        Supplier supplier = new Supplier();
        supplier.supplierId = UUID.randomUUID().toString();
        supplier.supplierName = supplierName;
        supplier.supplierType = supplierType;
        supplier.phoneNumber = phoneNumber;
        supplier.address = address;
        supplier.suppliedProductType = suppliedProductType;

        supplier.currentDebt = BigDecimal.ZERO;

        return supplier;
    }

    // =================================================================
    // 2. FACTORY: TÁI TẠO (Load từ DB)
    // =================================================================
    public static Supplier reconstitute(
            String supplierId,
            String supplierName,
            String supplierType,
            String phoneNumber,
            String address,
            String suppliedProductType,
            BigDecimal currentDebt
    ) {
        Supplier supplier = new Supplier();
        supplier.supplierId = supplierId;
        supplier.supplierName = supplierName;
        supplier.supplierType = supplierType;
        supplier.phoneNumber = phoneNumber;
        supplier.address = address;
        supplier.suppliedProductType = suppliedProductType;
        supplier.currentDebt = currentDebt;
        return supplier;
    }

    // =================================================================
    // 3. BUSINESS: CẬP NHẬT THÔNG TIN (Info)
    // Đổi thành void để đúng chuẩn State Mutation
    // =================================================================
    public void updateInfo(String supplierName, String supplierType, String phoneNumber, String address, String suppliedProductType) {
        if (supplierName != null && !supplierName.isBlank()) this.supplierName = supplierName;
        if (supplierType != null && !supplierType.isBlank()) this.supplierType = supplierType;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (address != null && !address.isBlank()) this.address = address;
        if (suppliedProductType != null && !suppliedProductType.isBlank()) this.suppliedProductType = suppliedProductType;
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
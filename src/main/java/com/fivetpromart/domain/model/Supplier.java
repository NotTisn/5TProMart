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
    private String address;
    private String phoneNumber;
    private String representName;
    private String representPhoneNumber;
    private String supplierType;
    private String suppliedProductType;
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
            String suppliedProductType
    ) {
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
        supplier.address = address;
        supplier.phoneNumber = phoneNumber;
        supplier.representName = representName;
        supplier.representPhoneNumber = representPhoneNumber;
        supplier.supplierType = supplierType;
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
            String address,
            String phoneNumber,
            String representName,
            String representPhoneNumber,
            String supplierType,
            String suppliedProductType,
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
        supplier.suppliedProductType = suppliedProductType;
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
            String suppliedProductType
    ) {
        if (supplierName != null && !supplierName.isBlank()) this.supplierName = supplierName;
        if (address != null && !address.isBlank()) this.address = address;
        if (phoneNumber != null && !phoneNumber.isBlank()) this.phoneNumber = phoneNumber;
        if (representName != null) this.representName = representName;  // Allow empty for optional field
        if (representPhoneNumber != null) this.representPhoneNumber = representPhoneNumber;  // Allow empty for optional field
        if (supplierType != null && !supplierType.isBlank()) this.supplierType = supplierType;
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
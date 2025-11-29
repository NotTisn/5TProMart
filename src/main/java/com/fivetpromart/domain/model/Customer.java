package com.fivetpromart.domain.model;

import com.fivetpromart.infrastructure.error.AppException;
import com.fivetpromart.infrastructure.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

    private String customerId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private LocalDate registrationDate;
    private long loyaltyPoints;

    // =================================================================
    // 1. FACTORY METHOD: TẠO MỚI (Business Logic)
    // Dùng khi nhân viên tạo hồ sơ khách hàng mới
    // =================================================================
    public static Customer create(String fullName, String phoneNumber, String gender, LocalDate dob) {
        // Validate dữ liệu đầu vào cơ bản (nếu cần)
        if (fullName == null || fullName.isBlank()) {
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY);
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new AppException(ErrorCode.CANNOT_BE_EMPTY);
        }
        if (phoneNumber.length() != 10) {
            throw new AppException(ErrorCode.INVALID_PHONE);
        }

        Customer customer = new Customer();
        customer.customerId = UUID.randomUUID().toString(); // Tự sinh ID
        customer.fullName = fullName;
        customer.phoneNumber = phoneNumber;
        customer.gender = gender;
        customer.dateOfBirth = dob;

        // Giá trị mặc định khi mới tạo
        customer.registrationDate = LocalDate.now();
        customer.loyaltyPoints = 0; // Mới tạo thì chưa có điểm

        return customer;
    }

    // =================================================================
    // 2. RECONSTITUTION FACTORY: TÁI TẠO (Infrastructure Logic)
    // Dùng cho Mapper để đổ dữ liệu từ DBO lên Domain
    // =================================================================
    public static Customer reconstitute(
            String id,
            String fullName,
            String gender,
            LocalDate dateOfBirth,
            String phoneNumber,
            LocalDate registrationDate,
            long loyaltyPoints
    ) {
        Customer customer = new Customer();
        customer.customerId = id; // Giữ ID cũ
        customer.fullName = fullName;
        customer.gender = gender;
        customer.dateOfBirth = dateOfBirth;
        customer.phoneNumber = phoneNumber;
        customer.registrationDate = registrationDate;
        customer.loyaltyPoints = loyaltyPoints; // Giữ nguyên điểm cũ
        return customer;
    }

    // =================================================================
    // 3. BUSINESS BEHAVIORS (Hành vi nghiệp vụ)
    // Thay vì Setter, ta dùng các hàm có ý nghĩa
    // =================================================================

    /**
     * Cập nhật thông tin cá nhân
     */
    public void updateProfile(String fullName, String gender, LocalDate dateOfBirth) {
        if (fullName != null && !fullName.isBlank()) {
            this.fullName = fullName;
        }
        if (gender != null && !gender.isBlank()) {
            this.gender = gender;
        }
        if (dateOfBirth != null) {
            this.dateOfBirth = dateOfBirth;
        }
    }

    /**
     * Cập nhật số điện thoại (Logic nhạy cảm có thể cần verify)
     */
    public void changePhoneNumber(String newPhoneNumber) {
        if (newPhoneNumber == null || newPhoneNumber.isBlank()) {
            throw new IllegalArgumentException("New phone number cannot be empty");
        }
        if (newPhoneNumber.length() != 10) {
            throw new AppException(ErrorCode.INVALID_PHONE);
        }
        this.phoneNumber = newPhoneNumber;
    }

    /**
     * Tích điểm (Cộng điểm)
     */
    public void earnPoints(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Points amount must be positive");
        }
        this.loyaltyPoints += amount;
    }

    /**
     * Tiêu điểm (Trừ điểm)
     */
    public void redeemPoints(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Points amount must be positive");
        }
        if (this.loyaltyPoints < amount) {
            throw new IllegalStateException("Not enough loyalty points to redeem");
        }
        this.loyaltyPoints -= amount;
    }
}
package com.fivetpromart.infrastructure.persistence.customer;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //@Column(length = 36)
    String id;

    @Column(name = "full_name", nullable = false)
    String fullName;

    @Column(length = 10)
    String gender;

    @Column(name = "dob")
    LocalDate dateOfBirth;

    // Số điện thoại thường là Unique để tránh trùng khách hàng
    @Column(name = "phone_number", unique = true, length = 15, nullable = false)
    String phoneNumber;

    @Column(name = "registration_date", nullable = false)
    LocalDate registrationDate;

    // Nên set giá trị mặc định trong DB
    @Column(name = "loyalty_points", nullable = false)
    long loyaltyPoints;

    @Column(name = "created_at", updatable = false)
    Instant createdAt;

    @Column(name = "updated_at")
    Instant updatedAt;
}
package com.fivetpromart.infrastructure.persistence.customer.mapper;

import com.fivetpromart.domain.model.Customer;
import com.fivetpromart.infrastructure.persistence.customer.CustomerDbo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {

    // 1. DOMAIN -> DBO (Chiều này MapStruct tự làm được nếu tên field giống nhau)
    // Nhưng viết tường minh ra cũng tốt để kiểm soát
    default CustomerDbo toDbo(Customer domain) {
        if (domain == null) return null;

        return CustomerDbo.builder()
                .id(domain.getId())
                .fullName(domain.getFullName())
                .gender(domain.getGender())
                .dateOfBirth(domain.getDateOfBirth())
                .phoneNumber(domain.getPhoneNumber())
                .registrationDate(domain.getRegistrationDate())
                .loyaltyPoints(domain.getLoyaltyPoints())
                .build();
    }

    // 2. DBO -> DOMAIN (Chiều này BẮT BUỘC viết tay)
    // Để gọi hàm reconstitute
    default Customer toDomain(CustomerDbo dbo) {
        if (dbo == null) return null;

        return Customer.reconstitute(
                dbo.getId(),
                dbo.getFullName(),
                dbo.getGender(),
                dbo.getDateOfBirth(),
                dbo.getPhoneNumber(),
                dbo.getRegistrationDate(),
                dbo.getLoyaltyPoints()
        );
    }
}
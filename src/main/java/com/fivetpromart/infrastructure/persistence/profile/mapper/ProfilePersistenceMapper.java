package com.fivetpromart.infrastructure.persistence.profile.mapper;

import com.fivetpromart.domain.model.Profile;
import com.fivetpromart.infrastructure.persistence.profile.ProfileDbo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * GHI CHÚ QUAN TRỌNG VỀ LỖI "ArrayList":
 * * Nếu bạn thêm một List phức tạp (ví dụ: List<Preference>
 * vào Profile.java), bạn SẼ gặp lỗi "ArrayList".
 * * Để sửa, bạn phải tạo một Mapper "con" (ví dụ: PreferenceMapper.class)
 * và "đăng ký" nó ở đây, như sau:
 *
 * @Mapper(componentModel = "spring", uses = { PreferenceMapper.class })
 * * (Hiện tại, chúng ta không cần "uses" vì Profile còn đơn giản)
 */
@Mapper(componentModel = "spring")
public interface ProfilePersistenceMapper {

    // 1. Dịch từ Domain -> DBO
    // MapStruct tự động bỏ qua 'fullName' (vì DBO không có)
    ProfileDbo toDbo(Profile domainEntity);

    // 2. Dịch từ DBO -> Domain
    // Bỏ qua 'fullName' (vì DBO không có) để xử lý thủ công
    @Mapping(target = "fullName", ignore = true)
    Profile toDomain(ProfileDbo dbo);

    // 3. Xử lý logic 'fullName' sau khi map
    // (Đây là cách "sạch" để xử lý các trường suy ra)
    @AfterMapping
    default void setFullNameFromDbo(ProfileDbo dbo, @MappingTarget Profile.ProfileBuilder profileBuilder) {

        String firstName = dbo.getFirstName() != null ? dbo.getFirstName() : "";
        String lastName = dbo.getLastName() != null ? dbo.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();

        // Set giá trị vào builder
        profileBuilder.fullName(fullName);
    }
}
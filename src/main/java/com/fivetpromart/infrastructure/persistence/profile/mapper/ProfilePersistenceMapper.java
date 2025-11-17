package com.fivetpromart.infrastructure.persistence.profile;

import com.fivetpromart.domain.model.Profile;
import org.mapstruct.*;

@Mapper(componentModel = "spring") // 1. Báo MapStruct tạo ra Spring Bean
public interface ProfilePersistenceMapper {

    // === 2. Dịch từ Domain -> DBO ===
    // MapStruct tự động bỏ qua 'fullName' từ Profile
    // vì ProfileDbo không có trường đó.
    ProfileDbo toDbo(Profile domainEntity);

    // === 3. Dịch từ DBO -> Domain ===
    // Vì Profile của bạn dùng @Builder, MapStruct sẽ tự động dùng nó.
    // Chúng ta bảo nó bỏ qua 'fullName' (vì DBO không có)
    // để chúng ta xử lý thủ công ở dưới.
    @Mapping(target = "fullName", ignore = true)
    Profile toDomain(ProfileDbo dbo);

    // === 4. Xử lý logic 'fullName' ===
    // Đây là phần "phép thuật"
    // MapStruct cho phép bạn "can thiệp" sau khi nó map các trường
    // Nó biết bạn dùng builder, nên nó đưa cho bạn @MappingTarget là
    // một Profile.Builder
    @AfterMapping
    default void setFullNameFromDbo(ProfileDbo dbo, @MappingTarget Profile.ProfileBuilder profileBuilder) {

        // Chính là logic thủ công của bạn
        String firstName = dbo.getFirstName() != null ? dbo.getFirstName() : "";
        String lastName = dbo.getLastName() != null ? dbo.getLastName() : "";
        String fullName = (firstName + " " + lastName).trim();

        // Set giá trị vào builder
        profileBuilder.fullName(fullName);
    }
}
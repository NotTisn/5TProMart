package com.fivetpromart.infrastructure.persistence.signup_pending.adapter;

import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.domain.model.PendingRegistration;
import com.fivetpromart.infrastructure.persistence.signup_pending.mapper.SignUpPersistenceMapper;
import com.fivetpromart.infrastructure.persistence.signup_pending.repository.ISignUpRequestJpaRepository;
import com.fivetpromart.infrastructure.persistence.signup_pending.SignUpRequestDbo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SignUpRequestAdapter implements ISignUpRequestRepository {

    private final ISignUpRequestJpaRepository jpaRepository;
    private final SignUpPersistenceMapper mapper; // Dùng MapStruct

    @Override
    public Optional<PendingRegistration> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public PendingRegistration save(PendingRegistration domainEntity) {
        // 1. Tìm xem bản ghi đã tồn tại dưới DB chưa (dựa theo Business Key là Email)
        Optional<SignUpRequestDbo> existingDboOpt = jpaRepository.findByEmail(domainEntity.getEmail().toLowerCase());

        SignUpRequestDbo dboToSave;

        if (existingDboOpt.isPresent()) {
            // === CASE UPDATE ===
            // Lấy DBO cũ ra (để giữ nguyên ID khóa chính)
            dboToSave = existingDboOpt.get();

            // Map dữ liệu mới từ Domain đè vào DBO cũ
            // Bạn cần thêm method updateDboFromDomain trong Mapper (xem bước 2)
            mapper.updateDboFromDomain(dboToSave, domainEntity);
        } else {
            // === CASE INSERT ===
            // Chưa có -> Map mới hoàn toàn
            dboToSave = mapper.toDbo(domainEntity);
            }

        // 2. Lúc này Hibernate biết dboToSave có ID (nếu update) -> Sẽ chạy UPDATE
        SignUpRequestDbo savedDbo = jpaRepository.save(dboToSave);

        return mapper.toDomain(savedDbo);
    }

    @Override
    public void deleteByEmail(String email) {
        jpaRepository.deleteByEmail(email);
    }
}
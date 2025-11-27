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
    public PendingRegistration save(PendingRegistration pendingRegistration) {
        SignUpRequestDbo dbo = mapper.toDbo(pendingRegistration);
        SignUpRequestDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public void deleteByEmail(String email) {
        jpaRepository.deleteByEmail(email);
    }
}
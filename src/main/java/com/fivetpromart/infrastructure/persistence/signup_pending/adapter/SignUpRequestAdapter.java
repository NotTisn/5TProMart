package com.fivetpromart.infrastructure.persistence.signup_pending.adapter;

import com.fivetpromart.application.port.out.ISignUpRequestRepository;
import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.domain.model.SignUpRequest;
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
    public Optional<SignUpRequest> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public SignUpRequest save(SignUpRequest signUpRequest) {
        SignUpRequestDbo dbo = mapper.toDbo(signUpRequest);
        SignUpRequestDbo savedDbo = jpaRepository.save(dbo);
        return mapper.toDomain(savedDbo);
    }

    @Override
    public void deleteByEmail(String email) {
        jpaRepository.deleteByEmail(email);
    }
}
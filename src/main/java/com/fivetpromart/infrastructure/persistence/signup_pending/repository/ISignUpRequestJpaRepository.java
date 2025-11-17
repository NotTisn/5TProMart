package com.fivetpromart.infrastructure.persistence.signup_pending.repository;

import com.fivetpromart.infrastructure.persistence.signup_pending.SignUpRequestDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ISignUpRequestJpaRepository extends JpaRepository<SignUpRequestDbo, String> {
    Optional<SignUpRequestDbo> findByEmail(String email);
    boolean existsByEmail(String email);
    @Transactional
    void deleteByEmail(String signUpRequestDbo);
}
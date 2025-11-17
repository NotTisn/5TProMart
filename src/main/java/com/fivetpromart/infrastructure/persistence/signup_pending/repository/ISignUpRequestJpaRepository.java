package com.fivetpromart.infrastructure.persistence.signup_pending;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ISignUpRequestJpaRepository extends JpaRepository<SignUpRequestDbo, String> {
    Optional<SignUpRequestDbo> findByEmail(String email);
    boolean existsByEmail(String email);
}  
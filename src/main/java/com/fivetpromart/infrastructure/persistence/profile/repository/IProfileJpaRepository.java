package com.fivetpromart.infrastructure.persistence.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IProfileJpaRepository extends JpaRepository<ProfileDbo, String> {

    Optional<ProfileDbo> findByUserId(String userId);
}
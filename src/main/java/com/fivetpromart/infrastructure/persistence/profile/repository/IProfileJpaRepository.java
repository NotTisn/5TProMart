package com.fivetpromart.infrastructure.persistence.profile.repository;

import com.fivetpromart.infrastructure.persistence.profile.ProfileDbo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IProfileJpaRepository extends JpaRepository<ProfileDbo, String> {

    Optional<ProfileDbo> findByUserId(String userId);
}
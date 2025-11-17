package com.fivetpromart.infrastructure.persistence.profile.adapter;



import com.fivetpromart.application.port.out.IProfileRepository;

import com.fivetpromart.domain.model.Profile;

import com.fivetpromart.infrastructure.persistence.profile.ProfileDbo;
import com.fivetpromart.infrastructure.persistence.profile.mapper.ProfilePersistenceMapper;
import com.fivetpromart.infrastructure.persistence.profile.repository.IProfileJpaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;



import java.util.Optional;



@Repository // Đây là Adapter, implement Port của Application

@RequiredArgsConstructor

public class ProfileAdapter implements IProfileRepository {



    private final IProfileJpaRepository jpaRepository; // Dùng "phép thuật"

    private final ProfilePersistenceMapper mapper;     // Dùng "phiên dịch"



    @Override

    public Profile save(Profile profile) {

        // 1. Dịch Domain -> DBO

        ProfileDbo dbo = mapper.toDbo(profile);



        // 2. Dùng "phép thuật" JPA để lưu

        ProfileDbo savedDbo = jpaRepository.save(dbo);



        // 3. Dịch DBO (đã lưu) -> Domain để trả về

        return mapper.toDomain(savedDbo);

    }



    @Override

    public Optional<Profile> findByUserId(String userId) {

        // 1. Dùng "phép thuật" JPA để tìm

        Optional<ProfileDbo> optionalDbo = jpaRepository.findByUserId(userId);



        // 2. Dịch DBO (nếu tìm thấy) -> Domain

        return optionalDbo.map(mapper::toDomain); // Tương đương: .map(dbo -> mapper.toDomain(dbo))

    }

}
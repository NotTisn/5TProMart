package com.fivetpromart.application.port.out;

// Dùng DTO "sạch" của Application, KHÔNG dùng DBO
import com.fivetpromart.application.dto.SignUpRequestDto;
import com.fivetpromart.domain.model.SignUpRequest;

import java.util.Optional;

// Port này dùng DTO của Application (sẽ tạo ở dưới)
public interface ISignUpRequestRepository {
    Optional<SignUpRequest> findByEmail(String email);
    boolean existsByEmail(String email);
    SignUpRequest save(SignUpRequest signUpRequest);
    void deleteByEmail(String email);
}
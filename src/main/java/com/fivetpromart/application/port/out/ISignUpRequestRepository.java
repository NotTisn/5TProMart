package com.fivetpromart.application.port.out;

// Dùng DTO "sạch" của Application, KHÔNG dùng DBO
import com.fivetpromart.application.dto.SignUpRequestDto;
import java.util.Optional;

// Port này dùng DTO của Application (sẽ tạo ở dưới)
public interface ISignUpRequestRepository {
    Optional<SignUpRequestDto> findByEmail(String email);
    boolean existsByEmail(String email);
    SignUpRequestDto save(SignUpRequestDto signUpRequest);
    void deleteByEmail(String email);
}
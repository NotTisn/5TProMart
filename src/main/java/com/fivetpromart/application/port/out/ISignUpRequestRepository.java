package com.fivetpromart.application.port.out;

// Dùng DTO "sạch" của Application, KHÔNG dùng DBO
import com.fivetpromart.domain.model.PendingRegistration;

import java.util.Optional;

// Port này dùng DTO của Application (sẽ tạo ở dưới)
public interface ISignUpRequestRepository {
    Optional<PendingRegistration> findByEmail(String email);
    boolean existsByEmail(String email);
    PendingRegistration save(PendingRegistration pendingRegistration);
    void deleteByEmail(String email);
}
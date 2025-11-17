package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.User;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> findByEmail(String email);
    User save(User user);
    Optional<User> findById(String id);

}

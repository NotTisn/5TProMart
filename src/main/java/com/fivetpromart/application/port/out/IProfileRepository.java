package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Profile;
import java.util.Optional;

public interface IProfileRepository {

    Profile save(Profile profile);

    Optional<Profile> findByUserId(String userId);

    // ... các phương thức khác như delete, findById...
}
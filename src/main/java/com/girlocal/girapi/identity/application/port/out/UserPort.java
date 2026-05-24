package com.girlocal.girapi.identity.application.port.out;

import com.girlocal.girapi.identity.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserPort {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    void save(User user);
}

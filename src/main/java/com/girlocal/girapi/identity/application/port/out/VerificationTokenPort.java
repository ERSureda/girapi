package com.girlocal.girapi.identity.application.port.out;

import com.girlocal.girapi.identity.domain.model.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenPort {
    Optional<VerificationToken> findById(UUID id);
    void save(VerificationToken token);
    void delete(VerificationToken token);
}

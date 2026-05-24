package com.girlocal.girapi.profile.application.port.out;

import com.girlocal.girapi.profile.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfilePort {
    Optional<Profile> findByUserId(UUID userId);
    Profile save(Profile profile);
}

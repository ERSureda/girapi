package com.girlocal.girapi.identity.application.port.out;

import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;

import java.util.List;
import java.util.Optional;

public interface SessionPort {

    Optional<AuthenticatedUser> getSessionById(String token);
    List<AuthenticatedUser> getSessionsByUserId(String userId);
    void saveSession(String token, AuthenticatedUser authenticatedUser, long expirationEpoch);
    void deleteSessionById(String token);
    void deleteAllSessionsByUserId(String userId);
}


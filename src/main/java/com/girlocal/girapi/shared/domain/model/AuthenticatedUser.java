package com.girlocal.girapi.shared.domain.model;

public record AuthenticatedUser(
        String userId,
        String role
) {
}

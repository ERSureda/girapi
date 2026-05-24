package com.girlocal.girapi.profile.application.command;

import java.util.UUID;

public record InitializeProfileCommand(
        UUID userId,
        String firstName,
        String lastName
) {
}

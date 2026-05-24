package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record AdminForceResetUserCommand(
        String userId
) {
    public AdminForceResetUserCommand {
        CommandValidator.start()
                .rejectIfBlank(userId, "USER-ID_REQUIRED", "User ID cannot be blank.")
                .validate(AdminForceResetUserCommand.class.getSimpleName());
    }
}

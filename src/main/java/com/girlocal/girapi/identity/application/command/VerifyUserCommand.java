package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record VerifyUserCommand(
        String token
) {
    public VerifyUserCommand {
        CommandValidator.start()
                .rejectIfBlank(token, "TOKEN_REQUIRED", "Token cannot be null or empty.")
                .rejectIfInvalidUuid(token, "TOKEN_INVALID", "Token must be a valid UUID.")
                .validate(VerifyUserCommand.class.getSimpleName());
    }
}

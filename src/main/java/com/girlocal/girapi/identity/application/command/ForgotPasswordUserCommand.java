package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record ForgotPasswordUserCommand(
        String email
) {
    public ForgotPasswordUserCommand {
        CommandValidator.start()
                .rejectIfBlank(email, "EMAIL_REQUIRED", "Email cannot be blank.")
                .validate(ForgotPasswordUserCommand.class.getSimpleName());
    }
}

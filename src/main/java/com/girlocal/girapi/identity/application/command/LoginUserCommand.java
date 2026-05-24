package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record LoginUserCommand(
        String email,
        String password,
        boolean rememberMe
) {
    public LoginUserCommand {
        CommandValidator.start()
                .rejectIfBlank(email, "EMAIL_REQUIRED", "Email cannot be blank.")
                .rejectIfBlank(password, "PASSWORD_REQUIRED", "Password cannot be blank.")
                .validate(LoginUserCommand.class.getSimpleName());
    }
}

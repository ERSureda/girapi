package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record ResetPasswordUserCommand(
        String token,
        String newPassword,
        String matchingPassword
) {
    public ResetPasswordUserCommand {
        CommandValidator.start()
                .rejectIfBlank(token, "TOKEN_REQUIRED", "Token cannot be blank.")
                .rejectIfBlank(newPassword, "PASSWORD_REQUIRED", "Password cannot be blank.")
                .rejectIfBlank(matchingPassword, "MATCHING-PASSWORD_REQUIRED", "Matching password cannot be blank.")
                .validate(ResetPasswordUserCommand.class.getSimpleName());
    }
}

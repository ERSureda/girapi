package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record UpdatePasswordUserCommand(
        String password,
        String newPassword,
        String matchingPassword
) {
    public UpdatePasswordUserCommand {
        CommandValidator.start()
                .rejectIfBlank(password, "PASSWORD_REQUIRED", "Password cannot be blank.")
                .rejectIfBlank(newPassword, "NEW-PASSWORD_REQUIRED", "New password cannot be blank.")
                .rejectIfBlank(matchingPassword, "MATCHING-PASSWORD_REQUIRED", "Matching password cannot be blank.")
                .validate(UpdatePasswordUserCommand.class.getSimpleName());
    }
}

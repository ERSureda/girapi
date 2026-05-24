package com.girlocal.girapi.identity.application.command;

import com.girlocal.girapi.identity.domain.model.enums.UserRole;
import com.girlocal.girapi.shared.application.validation.CommandValidator;

public record RegisterUserCommand(
        String email,
        String password,
        String matchingPassword,
        String fullName,
        UserRole role
) {
    public RegisterUserCommand {
        CommandValidator.start()
                .rejectIfBlank(email, "EMAIL_REQUIRED", "Email cannot be blank.")
                .rejectIfBlank(password, "PASSWORD_REQUIRED", "Password cannot be blank.")
                .rejectIfBlank(matchingPassword, "MATCHING-PASSWORD_REQUIRED", "Matching password cannot be blank.")
                .rejectIfBlank(fullName, "FULL-NAME_REQUIRED", "Full name cannot be blank.")
                .rejectIfNull(role, "ROLE_REQUIRED", "Role cannot be null.")
                .validate(RegisterUserCommand.class.getSimpleName());
    }
}

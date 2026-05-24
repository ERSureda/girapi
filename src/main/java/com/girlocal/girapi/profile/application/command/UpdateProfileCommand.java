package com.girlocal.girapi.profile.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

import java.util.UUID;

public record UpdateProfileCommand(
        UUID userId,
        String firstName,
        String lastName,
        String phone
) {
    public UpdateProfileCommand {
        CommandValidator.start()
                .rejectIfNull(userId, "USER_ID_REQUIRED", "User ID is required.")
                .rejectIfBlank(firstName, "FIRST_NAME_REQUIRED", "First name is required.")
                .rejectIfBlank(lastName, "LAST_NAME_REQUIRED", "Last name is required.")
                .rejectIfBlank(phone, "PHONE_REQUIRED", "Phone number is required.")
                .validate(UpdateProfileCommand.class.getSimpleName());
    }
}

package com.girlocal.girapi.profile.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

import java.util.UUID;

public record DeleteAddressCommand(
        UUID addressId
) {
    public DeleteAddressCommand {
        CommandValidator.start()
                .rejectIfNull(addressId, "ADDRESS_ID_REQUIRED", "Address ID is required.")
                .validate(DeleteAddressCommand.class.getSimpleName());
    }
}

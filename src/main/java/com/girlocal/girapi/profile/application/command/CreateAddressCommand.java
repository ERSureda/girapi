package com.girlocal.girapi.profile.application.command;

import com.girlocal.girapi.shared.application.validation.CommandValidator;

import java.util.UUID;

public record CreateAddressCommand(
        UUID userId,
        String addressLine1,
        String addressLine2,
        String locality,
        String administrativeArea,
        String postalCode,
        String countryCode
) {
    public CreateAddressCommand {
        CommandValidator.start()
                .rejectIfNull(userId, "USER_ID_REQUIRED", "User ID is required.")
                .rejectIfBlank(addressLine1, "ADDRESS_LINE1_REQUIRED", "Address line 1 is required.")
                .rejectIfBlank(locality, "LOCALITY_REQUIRED", "Locality is required.")
                .rejectIfBlank(postalCode, "POSTAL_CODE_REQUIRED", "Postal code is required.")
                .rejectIfBlank(countryCode, "COUNTRY_CODE_REQUIRED", "Country code is required.")
                .validate(CreateAddressCommand.class.getSimpleName());
    }
}

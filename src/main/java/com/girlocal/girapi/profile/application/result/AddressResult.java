package com.girlocal.girapi.profile.application.result;

import java.util.UUID;

public record AddressResult(
        UUID id,
        String addressLine1,
        String addressLine2,
        String locality,
        String administrativeArea,
        String postalCode,
        String countryCode
) {
}

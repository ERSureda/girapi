package com.girlocal.girapi.profile.application.result;

import java.util.List;
import java.util.UUID;

public record ProfileResult(
        UUID userId,
        String firstName,
        String lastName,
        String phone,
        AddressResult address
) {
}

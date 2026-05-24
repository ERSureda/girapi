package com.girlocal.girapi.profile.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAddressHttpRequest(
        @Schema(description = "The first line of the address.", example = "Gran Via 123, 4B")
        @NotBlank(message = "ADDRESS-LINE1_REQUIRED")
        String addressLine1,

        @Schema(description = "Optional second line of the address.", example = "Escalera Izquierda")
        String addressLine2,

        @Schema(description = "The locality/city of the address.", example = "Madrid")
        @NotBlank(message = "LOCALITY_REQUIRED")
        String locality,

        @Schema(description = "Optional state, province, or region.", example = "Madrid")
        String administrativeArea,

        @Schema(description = "The postal/ZIP code.", example = "28013")
        @NotBlank(message = "POSTAL-CODE_REQUIRED")
        String postalCode,

        @Schema(description = "The ISO 2-letter country code.", example = "ES")
        @NotBlank(message = "COUNTRY-CODE_REQUIRED")
        @Size(min = 2, max = 2, message = "COUNTRY-CODE_INVALID")
        String countryCode
) {
}

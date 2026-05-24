package com.girlocal.girapi.profile.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileHttpRequest(
        @Schema(description = "The user's first name.", example = "John")
        @NotBlank(message = "FIRST-NAME_REQUIRED")
        String firstName,

        @Schema(description = "The user's last name.", example = "Doe")
        @NotBlank(message = "LAST-NAME_REQUIRED")
        String lastName,

        @Schema(description = "The user's phone number.", example = "+34612345678")
        @NotBlank(message = "PHONE_REQUIRED")
        String phone
) {
}

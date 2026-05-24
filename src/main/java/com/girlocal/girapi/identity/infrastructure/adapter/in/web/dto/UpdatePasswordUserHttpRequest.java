package com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordUserHttpRequest(
        @Schema(description = "The user's password.", example = "SecureP@ssw0rd")
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID-LENGTH")
        String password,

        @Schema(description = "The user's new password.", example = "NewSecureP@ssw0rd")
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID-LENGTH")
        String newPassword,

        @Schema(description = "The user's matching password.", example = "NewSecureP@ssw0rd")
        @NotBlank(message = "MATCHING-PASSWORD_REQUIRED")
        String matchingPassword
) {
}

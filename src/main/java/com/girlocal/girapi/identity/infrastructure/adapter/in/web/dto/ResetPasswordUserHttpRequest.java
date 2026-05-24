package com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordUserHttpRequest(
        @Schema(description = "The password reset token sent to the user's email.", example = "(UUID)")
        @NotBlank(message = "TOKEN_REQUIRED")
        String token,

        @Schema(description = "The user's new password.", example = "NewSecureP@ssw0rd")
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID-LENGTH")
        String newPassword,

        @Schema(description = "The user's matching password.", example = "NewSecureP@ssw0rd")
        @NotBlank(message = "MATCHING-PASSWORD_REQUIRED")
        String matchingPassword
) {
}

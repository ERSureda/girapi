package com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserHttpRequest(
        @Schema(description = "The user's email address.", example = "test.testat@example.com")
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID-FORMAT")
        String email,

        @Schema(description = "The user's password.", example = "SecureP@ssw0rd")
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID-LENGTH")
        String password,

        @Schema(description = "Whether to remember the user for future logins.", example = "true")
        boolean rememberMe
) {
}

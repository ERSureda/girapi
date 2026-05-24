package com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordUserHttpRequest(
        @Schema(description = "The user's email address.", example = "test.testat@example.com")
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID-FORMAT")
        String email
) {
}

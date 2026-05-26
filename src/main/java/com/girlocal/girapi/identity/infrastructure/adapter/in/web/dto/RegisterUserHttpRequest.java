package com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto;

import com.girlocal.girapi.identity.domain.model.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserHttpRequest(
        @Schema(description = "The user's email address.", example = "test.testat@example.com")
        @NotBlank(message = "EMAIL_REQUIRED")
        @Email(message = "EMAIL_INVALID-FORMAT")
        String email,

        @Schema(description = "The user's new password.", example = "SecureP@ssw0rd")
        @NotBlank(message = "PASSWORD_REQUIRED")
        @Size(min = 8, message = "PASSWORD_INVALID-LENGTH")
        String password,

        @Schema(description = "The user's matching password.", example = "SecureP@ssw0rd")
        @NotBlank(message = "MATCHING-PASSWORD_REQUIRED")
        String matchingPassword,

        @Schema(description = "The user's first name.", example = "Test")
        @NotBlank(message = "FIRST-NAME_REQUIRED")
        String firstName,

        @Schema(description = "The user's last name.", example = "Testat")
        @NotBlank(message = "LAST-NAME_REQUIRED")
        String lastName,

        @Schema(description = "The user's role.", example = "CUSTOMER")
        @NotNull(message = "ROLE_REQUIRED")
        UserRole role
) {
}

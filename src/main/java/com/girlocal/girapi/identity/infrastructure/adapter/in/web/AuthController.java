package com.girlocal.girapi.identity.infrastructure.adapter.in.web;

import com.girlocal.girapi.identity.application.port.in.*;
import com.girlocal.girapi.identity.application.result.LoginUserResult;
import com.girlocal.girapi.identity.infrastructure.adapter.in.web.dto.*;
import com.girlocal.girapi.identity.infrastructure.adapter.in.web.mapper.AuthWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Access and session management endpoint.")
public class AuthController {

    private final AuthWebMapper mapper;

    private final LoginUserUseCase loginUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final LogoutUserUseCase logoutUserUseCase;
    private final VerifyUserUseCase verifyUserUseCase;
    private final ForgotPasswordUserUseCase forgotPasswordUserUseCase;
    private final ResetPasswordUserUseCase resetPasswordUserUseCase;
    private final UpdatePasswordUserUseCase updatePasswordUserUseCase;

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with their credentials and returns an access token if successful."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful, access token returned."),
            @ApiResponse(responseCode = "400", description = "Invalid login request (e.g. missing fields, invalid format)."),
            @ApiResponse(responseCode = "401", description = "Authentication failed (e.g. incorrect credentials, unverified email).")
    })
    public ResponseEntity<LoginUserResult> login(
            @Valid @RequestBody LoginUserHttpRequest request
    ) {
        return ResponseEntity
                .status(200)
                .body(loginUserUseCase.execute(mapper.toLoginUserCommand(request)));
    }

    @PostMapping("/register")
    @Operation(
            summary = "User registration",
            description = "Registers a new user with the provided information. An email verification token will be sent to the user's email address."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registration successful, verification email sent."),
            @ApiResponse(responseCode = "400", description = "Invalid registration request (e.g. missing fields, invalid format)."),
            @ApiResponse(responseCode = "409", description = "Registration failed due to conflict (e.g. email already in use).")
    })
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterUserHttpRequest request
    ) {
        registerUserUseCase.execute(mapper.toRegisterUserCommand(request));
        return ResponseEntity
                .status(201)
                .build();
    }

    @PostMapping("/logout")
    @Operation(
            summary = "User logout",
            description = "Logs out the user by invalidating their current access token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful."),
            @ApiResponse(responseCode = "400", description = "Invalid logout request (e.g. missing or malformed token)."),
            @ApiResponse(responseCode = "401", description = "Logout failed due to authentication issues (e.g. invalid or expired token).")
    })
    public ResponseEntity<Void> logout() {
        logoutUserUseCase.execute();
        return ResponseEntity
                .status(200)
                .build();
    }
    
    @PostMapping("/verify/{token}")
    @Operation(
            summary = "Email verification",
            description = "Verifies the user's email address using the token provided in the URL path."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email verification successful."),
            @ApiResponse(responseCode = "400", description = "Invalid verification request (e.g. missing or malformed token)."),
            @ApiResponse(responseCode = "401", description = "Email verification failed due to authentication issues (e.g. invalid or expired token).")
    })
    public ResponseEntity<Void> verify(
            @PathVariable String token
    ) {
        verifyUserUseCase.execute(mapper.toVerifyUserCommand(token));
        return ResponseEntity
                .status(200)
                .build();
    }

    @PostMapping("/forgot-password")
    @Operation(
            summary = "Forgot password",
            description = "Initiates the password reset process by sending a reset token to the user's email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Forgot password request accepted. If the email exists, a reset token will be sent."),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g. missing or malformed email).")
    })
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordUserHttpRequest request
    ) {
        forgotPasswordUserUseCase.execute(mapper.toForgotPasswordUserCommand(request));
        return ResponseEntity
                .status(200)
                .build();
    }

    @PostMapping("/reset-password/{token}")
    @Operation(
            summary = "Reset password",
            description = "Resets the user's password using the token sent to their email."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g. missing fields, weak password)."),
            @ApiResponse(responseCode = "401", description = "Password reset failed (e.g. invalid or expired token).")
    })
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordUserHttpRequest request
    ) {
        resetPasswordUserUseCase.execute(mapper.toResetPasswordUserCommand(request));
        return ResponseEntity
                .status(200)
                .build();
    }

    @PutMapping("/update-password")
    @Operation(
            summary = "Update password",
            description = "Updates the authenticated user's password."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g. missing fields, weak new password)."),
            @ApiResponse(responseCode = "401", description = "Password update failed (e.g. incorrect current password, unauthenticated).")
    })
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordUserHttpRequest request
    ) {
        updatePasswordUserUseCase.execute(mapper.toUpdatePasswordUserCommand(request));
        return ResponseEntity
                .status(200)
                .build();
    }
}

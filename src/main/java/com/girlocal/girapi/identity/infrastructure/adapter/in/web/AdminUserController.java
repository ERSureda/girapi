package com.girlocal.girapi.identity.infrastructure.adapter.in.web;

import com.girlocal.girapi.identity.application.port.in.AdminForceResetUserUseCase;
import com.girlocal.girapi.identity.infrastructure.adapter.in.web.mapper.AdminUserWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin User Management", description = "API for administrators: manage users.")
public class AdminUserController {

    private final AdminUserWebMapper mapper;

    private final AdminForceResetUserUseCase adminForceResetUserUseCase;

    @PostMapping("/force-reset/{userId}")
    @Operation(
            summary = "Force password reset",
            description = "Forces an immediate password reset for the specified user and deletes all active sessions. The user will be required to set a new password upon their next login."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password reset forced successfully."),
            @ApiResponse(responseCode = "403", description = "Caller does not have administrator privileges."),
            @ApiResponse(responseCode = "404", description = "User not found.")
    })
    public ResponseEntity<Void> forceResetUserPassword(
            @PathVariable String userId
    ) {
        adminForceResetUserUseCase.execute(mapper.toAdminForceResetUserCommand(userId));
        return ResponseEntity
                .status(204)
                .build();
    }
}

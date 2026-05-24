package com.girlocal.girapi.profile.infrastructure.adapter.in.web;

import com.girlocal.girapi.profile.application.port.in.*;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.infrastructure.adapter.in.web.dto.*;
import com.girlocal.girapi.profile.infrastructure.adapter.in.web.mapper.ProfileWebMapper;
import com.girlocal.girapi.shared.application.port.in.SessionValidatorPort;
import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profile Management", description = "API for profile and address management.")
public class ProfileController {

    private final ProfileWebMapper mapper;

    private final GetProfileUseCase getProfileUseCase;
    private final UpdateProfileUseCase updateProfileUseCase;
    private final CreateAddressUseCase createAddressUseCase;
    private final UpdateAddressUseCase updateAddressUseCase;
    private final DeleteAddressUseCase deleteAddressUseCase;

    @GetMapping("/me")
    @Operation(
            summary = "Get authenticated user profile",
            description = "Retrieves the profile details and active address of the authenticated user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully."),
    })
    public ResponseEntity<ProfileResult> getMyProfile() {
        return ResponseEntity
                .status(200)
                .body(getProfileUseCase.execute());
    }

    @PutMapping("/me")
    @Operation(
            summary = "Update authenticated user profile",
            description = "Updates the authenticated user's basic personal details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully."),
    })
    public ResponseEntity<Void> updateMyProfile(
            @Valid @RequestBody UpdateProfileHttpRequest request
    ) {
        updateProfileUseCase.execute(mapper.toUpdateProfileCommand(request));
        return ResponseEntity
                .status(200)
                .build();
    }

    @PostMapping("/me/addresses")
    @Operation(
            summary = "Create authenticated user address",
            description = "Creates a new physical address and links it to the authenticated user's profile."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully."),
    })
    public ResponseEntity<Void> createMyAddress(
            @Valid @RequestBody CreateAddressHttpRequest request
    ) {
        createAddressUseCase.execute(mapper.toCreateAddressCommand(request));
        return ResponseEntity
                .status(201)
                .build();
    }

    @PutMapping("/me/addresses/{addressId}")
    @Operation(
            summary = "Update authenticated user address",
            description = "Updates an existing address within the authenticated user's profile."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully."),
    })
    public ResponseEntity<Void> updateMyAddress(
            @PathVariable UUID addressId,
            @Valid @RequestBody UpdateAddressHttpRequest request
    ) {
        updateAddressUseCase.execute(mapper.toUpdateAddressCommand(addressId, request));
        return ResponseEntity
                .status(200)
                .build();
    }

    @DeleteMapping("/me/addresses/{addressId}")
    @Operation(
            summary = "Delete authenticated user address",
            description = "Removes a specific address from the authenticated user's profile."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address deleted successfully."),
    })
    public ResponseEntity<Void> deleteMyAddress(
            @PathVariable UUID addressId
    ) {
        return ResponseEntity
                .status(200)
                .build();
    }
}

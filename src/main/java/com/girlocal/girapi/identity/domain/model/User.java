package com.girlocal.girapi.identity.domain.model;

import com.girlocal.girapi.identity.domain.event.ForgotPasswordEvent;
import com.girlocal.girapi.identity.domain.event.ResetPasswordEvent;
import com.girlocal.girapi.identity.domain.event.VerifyNewUserEvent;
import com.girlocal.girapi.identity.domain.model.enums.UserRole;
import com.girlocal.girapi.identity.domain.model.enums.UserStatus;
import com.girlocal.girapi.shared.domain.event.InitializeProfileEvent;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.model.AggregateRoot;

import java.util.UUID;

public class User extends AggregateRoot<UUID> {

    private final String email;
    private String passwordHash;
    private UserRole role;
    private UserStatus status;

    /// --- Constructors ---
    private User(
            UUID id,
            String email,
            String passwordHash,
            UserRole role,
            UserStatus status
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.status = status;

        this.validateData();
    }

    public static User create(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            UserRole role,
            UUID verificationToken
    ) {
        if (verificationToken == null)
            throw new DomainException("VERIFICATION-TOKEN_CANNOT_BE_NULL", "User verificationToken is required.");

        User user = new User(
                id,
                email,
                passwordHash,
                role,
                UserStatus.PENDING_VERIFICATION
        );

        user.registerEvent(new InitializeProfileEvent(
                id,
                firstName,
                lastName
        ));

        user.registerEvent(new VerifyNewUserEvent(
                user.email,
                firstName + " " + lastName,
                verificationToken.toString()
        ));

        return user;
    }

    public static User reconstruct(
            UUID id,
            String email,
            String passwordHash,
            UserRole role,
            UserStatus status
    ) {
        return new User(
                id,
                email,
                passwordHash,
                role,
                status
        );
    }

    /// --- Getters ---
    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    /// --- Business Logic ---
    private void validateData() {
        if (id == null)
            throw new DomainException("ID_CANNOT_BE_NULL", "User Id is required.");
        if (email == null || email.isBlank())
            throw new DomainException("EMAIL_CANNOT_BE_EMPTY", "User email cannot be empty.");
        if (passwordHash == null || passwordHash.isBlank())
            throw new DomainException("PASSWORD-HASH_CANNOT_BE_EMPTY", "User passwordHash cannot be empty.");
        if (role == null)
            throw new DomainException("ROLE_CANNOT_BE_NULL", "User role is required.");
        if (status == null)
            throw new DomainException("STATUS_CANNOT_BE_NULL", "User status is required.");
    }

    private void validateCanRequestPasswordReset() {
        if (this.status == UserStatus.DELETED)
            throw new DomainException("USER_DELETED", "User has been deleted.");
    }

    public void validateCanLogin() {
        switch (this.status) {
            case PENDING_VERIFICATION -> throw new DomainException("USER_PENDING_VERIFICATION", "User is not verified.");
            case DELETED -> throw new DomainException("USER_DELETED", "User has been deleted.");
            case LOCKED -> throw new DomainException("USER_LOCKED", "User has been locked.");
            case SUSPENDED -> throw new DomainException("USER_SUSPENDED", "User has been suspended.");
            case ACTIVE -> {}
            default -> throw new DomainException("USER_INVALID_STATUS", "User status does not allow login.");
        }
    }

    public void requestForgotPassword(UUID resetToken) {
        this.validateCanRequestPasswordReset();
        this.registerEvent(new ForgotPasswordEvent(
                this.email,
                resetToken.toString()
        ));
    }

    public void requestResetPassword(UUID resetToken) {
        this.validateCanRequestPasswordReset();
        this.registerEvent(new ResetPasswordEvent(
                this.email,
                resetToken.toString()
        ));
    }
}

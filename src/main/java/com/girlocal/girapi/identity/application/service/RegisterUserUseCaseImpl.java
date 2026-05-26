package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.RegisterUserCommand;
import com.girlocal.girapi.identity.application.port.in.RegisterUserUseCase;
import com.girlocal.girapi.identity.application.port.out.PasswordEncoderPort;
import com.girlocal.girapi.identity.application.port.out.UserPort;
import com.girlocal.girapi.identity.application.port.out.VerificationTokenPort;
import com.girlocal.girapi.identity.domain.model.User;
import com.girlocal.girapi.identity.domain.model.VerificationToken;
import com.girlocal.girapi.identity.domain.model.enums.UserRole;
import com.girlocal.girapi.identity.domain.model.enums.VerificationTokenType;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import com.girlocal.girapi.shared.domain.exception.ValidationException;
import com.girlocal.girapi.shared.infraestructure.utils.UuidGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserPort userPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final VerificationTokenPort verificationTokenPort;

    @Override
    @Transactional
    public void execute(RegisterUserCommand command) {
        if (!command.password().equals(command.matchingPassword())) {
            throw new DomainException("PASSWORD_MISMATCH", "Password and matching password do not match.");
        }

        if (userPort.existsByEmail(command.email())) {
            throw new DomainException("INVALID_CREDENTIALS", "A user with this email already exists.");
        }

        if (command.role() == UserRole.ADMIN) {
            throw new DomainException("INVALID_ROLE", "Cannot assign ADMIN role during registration.");
        }

        UUID userId = UuidGenerator.generateId();
        UUID verificationTokenId = UuidGenerator.generateId();

        String passwordHash = passwordEncoderPort.encode(command.password());

        VerificationToken verificationToken = VerificationToken.create(
                verificationTokenId,
                userId,
                VerificationTokenType.VERIFY_NEW_USER,
                Instant.now().plus(1, ChronoUnit.HOURS)
        );

        User newUser = User.create(
                userId,
                command.firstName(),
                command.lastName(),
                command.email(),
                passwordHash,
                command.role(),
                verificationTokenId
        );

        verificationTokenPort.save(verificationToken);
        userPort.save(newUser);
    }
}

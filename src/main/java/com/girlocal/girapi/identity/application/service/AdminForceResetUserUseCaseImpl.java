package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.AdminForceResetUserCommand;
import com.girlocal.girapi.identity.application.port.in.AdminForceResetUserUseCase;
import com.girlocal.girapi.shared.application.port.in.SessionValidatorPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminForceResetUserUseCaseImpl implements AdminForceResetUserUseCase {

    private final SessionValidatorPort sessionValidatorPort;

    @Override
    @Transactional
    public void execute(AdminForceResetUserCommand command) {
        sessionValidatorPort.requireAdminSession();
    }
}

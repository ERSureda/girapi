package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.port.in.LogoutUserUseCase;
import com.girlocal.girapi.identity.application.port.out.SessionPort;
import com.girlocal.girapi.shared.application.port.in.CurrentUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUserUseCaseImpl implements LogoutUserUseCase {

    private final CurrentUserPort currentUserPort;
    private final SessionPort sessionPort;

    @Override
    public void execute() {
        sessionPort.deleteAllSessionsByUserId(currentUserPort.get().userId());
    }
}

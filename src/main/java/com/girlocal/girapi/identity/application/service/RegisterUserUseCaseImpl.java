package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.RegisterUserCommand;
import com.girlocal.girapi.identity.application.port.in.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    @Override
    public void execute(RegisterUserCommand command) {

    }
}


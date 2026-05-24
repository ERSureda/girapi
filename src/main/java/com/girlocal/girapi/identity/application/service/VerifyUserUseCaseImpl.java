package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.VerifyUserCommand;
import com.girlocal.girapi.identity.application.port.in.VerifyUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyUserUseCaseImpl implements VerifyUserUseCase {

    @Override
    public void execute(VerifyUserCommand command) {

    }
}


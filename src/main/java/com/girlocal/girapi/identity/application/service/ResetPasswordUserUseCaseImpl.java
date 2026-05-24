package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.ResetPasswordUserCommand;
import com.girlocal.girapi.identity.application.port.in.ResetPasswordUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordUserUseCaseImpl implements ResetPasswordUserUseCase {

    @Override
    public void execute(ResetPasswordUserCommand command) {

    }
}


package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.ForgotPasswordUserCommand;
import com.girlocal.girapi.identity.application.port.in.ForgotPasswordUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordUserUseCaseImpl implements ForgotPasswordUserUseCase {

    @Override
    public void execute(ForgotPasswordUserCommand command) {

    }
}

package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.LoginUserCommand;
import com.girlocal.girapi.identity.application.port.in.LoginUserUseCase;
import com.girlocal.girapi.identity.application.result.LoginUserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    @Override
    public LoginUserResult execute(LoginUserCommand command) {
        return null;
    }
}

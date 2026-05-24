package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.LoginUserCommand;
import com.girlocal.girapi.identity.application.result.LoginUserResult;

public interface LoginUserUseCase {
    LoginUserResult execute(LoginUserCommand command);
}

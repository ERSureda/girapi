package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.RegisterUserCommand;

public interface RegisterUserUseCase {
    void execute(RegisterUserCommand command);
}


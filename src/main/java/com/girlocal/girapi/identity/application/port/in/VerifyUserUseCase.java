package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.VerifyUserCommand;

public interface VerifyUserUseCase {
    void execute(VerifyUserCommand command);
}


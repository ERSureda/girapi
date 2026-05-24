package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.ResetPasswordUserCommand;

public interface ResetPasswordUserUseCase {
    void execute(ResetPasswordUserCommand command);
}


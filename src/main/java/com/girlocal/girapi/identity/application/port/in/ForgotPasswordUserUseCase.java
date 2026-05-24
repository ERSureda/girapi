package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.ForgotPasswordUserCommand;

public interface ForgotPasswordUserUseCase {
    void execute(ForgotPasswordUserCommand command);
}

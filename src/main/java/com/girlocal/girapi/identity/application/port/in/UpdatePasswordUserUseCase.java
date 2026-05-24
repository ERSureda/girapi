package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.UpdatePasswordUserCommand;

public interface UpdatePasswordUserUseCase {
    void execute(UpdatePasswordUserCommand command);
}


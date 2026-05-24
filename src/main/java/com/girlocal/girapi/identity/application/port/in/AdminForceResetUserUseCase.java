package com.girlocal.girapi.identity.application.port.in;

import com.girlocal.girapi.identity.application.command.AdminForceResetUserCommand;

public interface AdminForceResetUserUseCase {
    void execute(AdminForceResetUserCommand command);
}

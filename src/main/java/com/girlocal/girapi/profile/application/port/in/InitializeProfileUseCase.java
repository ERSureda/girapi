package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.command.InitializeProfileCommand;

public interface InitializeProfileUseCase {
    void execute(InitializeProfileCommand command);
}

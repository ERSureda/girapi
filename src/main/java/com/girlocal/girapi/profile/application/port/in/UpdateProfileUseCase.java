package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.command.UpdateProfileCommand;
import com.girlocal.girapi.profile.application.result.ProfileResult;

public interface UpdateProfileUseCase {
    void execute(UpdateProfileCommand command);
}

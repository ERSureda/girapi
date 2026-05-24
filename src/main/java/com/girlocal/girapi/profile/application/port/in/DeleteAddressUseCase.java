package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.command.DeleteAddressCommand;
import com.girlocal.girapi.profile.application.result.ProfileResult;

public interface DeleteAddressUseCase {
    void execute(DeleteAddressCommand command);
}

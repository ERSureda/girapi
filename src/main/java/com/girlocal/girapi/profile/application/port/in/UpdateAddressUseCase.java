package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.command.UpdateAddressCommand;
import com.girlocal.girapi.profile.application.result.ProfileResult;

public interface UpdateAddressUseCase {
    void execute(UpdateAddressCommand command);
}

package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.command.CreateAddressCommand;
import com.girlocal.girapi.profile.application.result.ProfileResult;

public interface CreateAddressUseCase {
    void execute(CreateAddressCommand command);
}

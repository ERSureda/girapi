package com.girlocal.girapi.profile.application.port.in;

import com.girlocal.girapi.profile.application.result.ProfileResult;

import java.util.UUID;

public interface GetProfileUseCase {
    ProfileResult execute();
}

package com.girlocal.girapi.profile.application.service;

import com.girlocal.girapi.profile.application.command.UpdateProfileCommand;
import com.girlocal.girapi.profile.application.port.in.UpdateProfileUseCase;
import com.girlocal.girapi.profile.application.port.out.ProfilePort;
import com.girlocal.girapi.profile.application.result.AddressResult;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateProfileUseCaseImpl implements UpdateProfileUseCase {

    private final ProfilePort profilePort;

    @Override
    @Transactional
    public void execute(UpdateProfileCommand command) {
    }
}

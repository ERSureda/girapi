package com.girlocal.girapi.profile.application.service;

import com.girlocal.girapi.profile.application.port.in.GetProfileUseCase;
import com.girlocal.girapi.profile.application.port.out.ProfilePort;
import com.girlocal.girapi.profile.application.result.AddressResult;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProfileUseCaseImpl implements GetProfileUseCase {

    private final ProfilePort profilePort;

    @Override
    @Transactional(readOnly = true)
    public ProfileResult execute() {
        return null;
    }
}

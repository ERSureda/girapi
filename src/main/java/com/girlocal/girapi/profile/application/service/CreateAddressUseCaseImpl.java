package com.girlocal.girapi.profile.application.service;

import com.girlocal.girapi.profile.application.command.CreateAddressCommand;
import com.girlocal.girapi.profile.application.port.in.CreateAddressUseCase;
import com.girlocal.girapi.profile.application.port.in.GetProfileUseCase;
import com.girlocal.girapi.profile.application.port.out.AddressPort;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.domain.model.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAddressUseCaseImpl implements CreateAddressUseCase {

    private final AddressPort addressPort;
    private final GetProfileUseCase getProfileUseCase;

    @Override
    @Transactional
    public void execute(CreateAddressCommand command) {
    }
}

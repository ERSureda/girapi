package com.girlocal.girapi.profile.application.service;

import com.girlocal.girapi.profile.application.command.UpdateAddressCommand;
import com.girlocal.girapi.profile.application.port.in.GetProfileUseCase;
import com.girlocal.girapi.profile.application.port.in.UpdateAddressUseCase;
import com.girlocal.girapi.profile.application.port.out.AddressPort;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.domain.model.Address;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateAddressUseCaseImpl implements UpdateAddressUseCase {

    private final AddressPort addressPort;
    private final GetProfileUseCase getProfileUseCase;

    @Override
    @Transactional
    public void execute(UpdateAddressCommand command) {
    }
}

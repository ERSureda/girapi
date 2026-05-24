package com.girlocal.girapi.profile.application.service;

import com.girlocal.girapi.profile.application.command.DeleteAddressCommand;
import com.girlocal.girapi.profile.application.port.in.DeleteAddressUseCase;
import com.girlocal.girapi.profile.application.port.in.GetProfileUseCase;
import com.girlocal.girapi.profile.application.port.out.AddressPort;
import com.girlocal.girapi.profile.application.port.out.ProfilePort;
import com.girlocal.girapi.profile.application.result.ProfileResult;
import com.girlocal.girapi.profile.domain.model.Address;
import com.girlocal.girapi.shared.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteAddressUseCaseImpl implements DeleteAddressUseCase {

    private final AddressPort addressPort;
    private final ProfilePort profilePort;
    private final GetProfileUseCase getProfileUseCase;

    @Override
    @Transactional
    public void execute(DeleteAddressCommand command) {
    }
}

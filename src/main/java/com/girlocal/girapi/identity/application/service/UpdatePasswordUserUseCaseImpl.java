package com.girlocal.girapi.identity.application.service;

import com.girlocal.girapi.identity.application.command.UpdatePasswordUserCommand;
import com.girlocal.girapi.identity.application.port.in.UpdatePasswordUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePasswordUserUseCaseImpl implements UpdatePasswordUserUseCase {

    @Override
    public void execute(UpdatePasswordUserCommand command) {

    }
}


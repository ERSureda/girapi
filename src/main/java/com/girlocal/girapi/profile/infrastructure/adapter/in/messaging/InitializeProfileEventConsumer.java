package com.girlocal.girapi.profile.infrastructure.adapter.in.messaging;

import com.girlocal.girapi.profile.application.command.InitializeProfileCommand;
import com.girlocal.girapi.profile.application.port.in.InitializeProfileUseCase;
import com.girlocal.girapi.shared.domain.event.InitializeProfileEvent;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeProfileEventConsumer {

    private final InitializeProfileUseCase initializeProfileUseCase;

    @SqsListener("initialize-profile-queue")
    public void consume(InitializeProfileEvent event) {
        InitializeProfileCommand command = new InitializeProfileCommand(
            event.userId(),
            event.firstName(),
            event.lastName()
        );

        initializeProfileUseCase.execute(command);
    }
}

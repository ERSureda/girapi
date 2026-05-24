package com.girlocal.girapi.shared.infraestructure.security;

import com.girlocal.girapi.shared.application.port.in.CurrentUserPort;
import com.girlocal.girapi.shared.application.port.in.SessionValidatorPort;
import com.girlocal.girapi.shared.domain.exception.ForbiddenException;
import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringSecuritySessionValidatorPort implements SessionValidatorPort {

    private final CurrentUserPort currentUserPort;

    @Override
    public AuthenticatedUser requiredSession() {
        AuthenticatedUser user = currentUserPort.get();
        if (user == null) {
            throw new ForbiddenException("No authenticated session found.");
        }
        return user;
    }

    @Override
    public AuthenticatedUser requireAdminSession() {
        AuthenticatedUser user = requiredSession();
        if (!"ADMIN".equals(user.role())) {
            throw new ForbiddenException("Admin privileges are required to access this resource.");
        }
        return user;
    }
}

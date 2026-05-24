package com.girlocal.girapi.shared.application.port.in;

import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;

public interface SessionValidatorPort {
    AuthenticatedUser requiredSession();
    AuthenticatedUser requireAdminSession();
}

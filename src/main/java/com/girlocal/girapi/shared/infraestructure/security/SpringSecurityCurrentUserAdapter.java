package com.girlocal.girapi.shared.infraestructure.security;

import com.girlocal.girapi.shared.application.port.in.CurrentUserPort;
import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityCurrentUserAdapter implements CurrentUserPort {
    @Override
    public AuthenticatedUser get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        return (AuthenticatedUser) authentication.getPrincipal();
    }
}

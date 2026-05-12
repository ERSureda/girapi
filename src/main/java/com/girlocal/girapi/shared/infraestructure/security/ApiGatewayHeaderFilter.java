package com.girlocal.girapi.shared.infraestructure.security;

import com.girlocal.girapi.shared.domain.model.AuthenticatedUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class ApiGatewayHeaderFilter extends OncePerRequestFilter {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String userId = request.getHeader(HEADER_USER_ID);
        final String userRole = request.getHeader(HEADER_USER_ROLE);


        if (userId != null && !userId.isBlank() && userRole != null && !userRole.isBlank()) {
            AuthenticatedUser authenticatedUser = new AuthenticatedUser(userId, userRole);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authenticatedUser,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.trace("User {} authenticated via API Gateway headers.", userId);
        }

        filterChain.doFilter(request, response);
    }
}

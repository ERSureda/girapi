package com.girlocal.girapi.identity.application.result;

public record LoginUserResult(
        String token,
        UserInfo user
) {
    public record UserInfo(
            String id,
            String email,
            String role
    ) {}
}

package com.girlocal.girapi.shared.domain.exception;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super("FORBIDDEN", message);
    }
}

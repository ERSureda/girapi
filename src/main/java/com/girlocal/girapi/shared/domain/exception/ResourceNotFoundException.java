package com.girlocal.girapi.shared.domain.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String code, String message) {
        super(code, message);
    }
}

package com.girlocal.girapi.shared.domain.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends BaseException {
    private final Map<String, String> validationErrors;

    public ValidationException(String message, Map<String, String> validationErrors) {
        super("VALIDATION_ERROR", message);
        this.validationErrors = validationErrors;
    }
}

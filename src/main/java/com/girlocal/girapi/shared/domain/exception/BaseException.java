package com.girlocal.girapi.shared.domain.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public abstract class BaseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    protected BaseException(String code, String message) {
        super(message);
        this.code = code;
    }
}

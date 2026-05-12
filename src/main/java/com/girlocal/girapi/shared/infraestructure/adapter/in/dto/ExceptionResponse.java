package com.girlocal.girapi.shared.infraestructure.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionResponse(
        String code,
        String message,
        Map<String, String> subErrors
) {
        public ExceptionResponse(String code) {
            this(code, null, null);
        }

        public ExceptionResponse(String code, String message) {
            this(code, message, null);
        }

        public ExceptionResponse(String code, Map<String, String> subErrors) {
            this(code, null, subErrors);
        }
}

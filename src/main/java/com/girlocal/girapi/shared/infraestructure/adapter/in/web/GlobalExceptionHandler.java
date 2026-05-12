package com.girlocal.girapi.shared.infraestructure.adapter.in.web;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.girlocal.girapi.shared.domain.exception.*;
import com.girlocal.girapi.shared.infraestructure.adapter.in.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Value("${application.errors.expose-details:false}")
    private boolean exposeErrorDetails;

    private ResponseEntity<ExceptionResponse> buildErrorResponse(
            BaseException e,
            HttpStatus status,
            Map<String, String> details
    ) {
        if (status.is5xxServerError()) {
            log.error("Server error: [{}] {}", e.getCode(), e.getMessage(), e);
            return ResponseEntity
                    .status(status)
                    .body(new ExceptionResponse(e.getCode(), null, null));
        }

        if (exposeErrorDetails) {
            log.warn("Client error: [{}] {} - Details: {}", e.getCode(), e.getMessage(), details);
            return ResponseEntity
                    .status(status)
                    .body(new ExceptionResponse(e.getCode(), e.getMessage(), details));
        }

        log.warn("Client error: [{}] {}", e.getCode(), e.getMessage());
        return ResponseEntity
                .status(status)
                .body(new ExceptionResponse(e.getCode(), null, details));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ExceptionResponse> handleDomainException(DomainException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<ExceptionResponse> handleInfrastructureException(InfrastructureException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, e.getValidationErrors());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> validationErrors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "INVALID_VALUE",
                        (existing, replacement) -> existing));
        log.warn("Validation failed: {}", validationErrors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("ERR_VALIDATION", null, validationErrors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof InvalidFormatException cause
                && cause.getTargetType() != null
                && cause.getTargetType().isEnum()) {

            String fieldName = cause.getPath().isEmpty() ? "unknown" : cause.getPath().getFirst().getFieldName();
            String validValues = Arrays.stream(cause.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            Map<String, String> errors = Map.of(fieldName, "INVALID_VALUE. Valid: " + validValues);
            log.warn("Invalid enum value '{}' for field '{}'", cause.getValue(), fieldName);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionResponse("ERR_VALIDATION", null, errors));
        }

        log.warn("Unreadable HTTP message: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse("ERR_INVALID_REQUEST", null, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("ERR_INTERNAL_SERVER", null, null));
    }
}

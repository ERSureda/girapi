package com.girlocal.girapi.shared.application.validation;

import com.girlocal.girapi.shared.domain.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Fluent builder for accumulating Command validation errors.
 * Designed for use inside compact record constructors.
 *
 * <pre>{@code
 * public MyCommand {
 *     CommandValidator.start()
 *         .rejectIfBlank(email, "EMAIL_CANNOT_BE_EMPTY", "Email cannot be null or empty.")
 *         .rejectIfNull(role,   "ROLE_CANNOT_BE_NULL",  "Role cannot be null.")
 *         .validate("MyCommand");
 * }
 * }</pre>
 */
public final class CommandValidator {

    private Map<String, String> errors;

    private CommandValidator() {}

    /** Creates a new validator instance. */
    public static CommandValidator start() {
        return new CommandValidator();
    }

    /**
     * Adds an error if {@code value} is null or blank.
     */
    public CommandValidator rejectIfBlank(String value, String code, String message) {
        if (value == null || value.isBlank()) {
            addError(code, message);
        }
        return this;
    }

    /**
     * Adds an error if {@code value} is null.
     */
    public CommandValidator rejectIfNull(Object value, String code, String message) {
        if (value == null) {
            addError(code, message);
        }
        return this;
    }

    /**
     * Adds an error when {@code condition} is true.
     * Use for custom rules (length checks, pattern matches, cross-field comparisons, etc.).
     */
    public CommandValidator rejectIf(boolean condition, String code, String message) {
        if (condition) {
            addError(code, message);
        }
        return this;
    }

    /**
     * Adds an error if {@code value} is not blank but fails UUID format validation.
     */
    public CommandValidator rejectIfInvalidUuid(String value, String code, String message) {
        if (value != null && !value.isBlank()) {
            try {
                UUID.fromString(value.trim());
            } catch (IllegalArgumentException e) {
                addError(code, message);
            }
        }
        return this;
    }

    /**
     * Throws {@link ValidationException} if any errors were accumulated; otherwise returns silently.
     *
     * @param commandName used as the exception message for traceability in logs.
     */
    public void validate(String commandName) {
        if (errors != null) {
            throw new ValidationException("Validation failed for " + commandName + ".", errors);
        }
    }

    // ── internals ────────────────────────────────────────────────────────────

    private void addError(String code, String message) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(code, message);
    }
}
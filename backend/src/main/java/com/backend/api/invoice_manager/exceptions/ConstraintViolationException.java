package com.backend.api.invoice_manager.exceptions;

import java.util.Set;

import jakarta.validation.ConstraintViolation;

public class ConstraintViolationException extends RuntimeException {
    private final Set<ConstraintViolation<?>> constraintViolations;
    private final long timestamp = System.currentTimeMillis();
    private final String errorCode = "VALIDATION_ERROR";

    public ConstraintViolationException(String message, Set<ConstraintViolation<?>> violations) {
        super(message);
        this.constraintViolations = violations;
    }

    public Set<ConstraintViolation<?>> getConstraintViolations() {
        return constraintViolations;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

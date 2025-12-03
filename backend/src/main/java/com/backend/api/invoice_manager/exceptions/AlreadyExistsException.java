package com.backend.api.invoice_manager.exceptions;

public class AlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String entity;
    private final String field;
    private final String value;

    public AlreadyExistsException(String message) {
        super(message);
        this.entity = null;
        this.field = null;
        this.value = null;
    }

    public AlreadyExistsException(String entity, String field, String value) {
        super(String.format("%s already exists with %s: %s", entity, field, value));
        this.entity = entity;
        this.field = field;
        this.value = value;
    }

    public String getEntity() {
        return entity;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}

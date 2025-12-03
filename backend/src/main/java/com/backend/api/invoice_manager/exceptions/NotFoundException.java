package com.backend.api.invoice_manager.exceptions;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String entity;
    private final String field;
    private final Object value;

    public NotFoundException(String message) {
        super(message);
        this.entity = null;
        this.field = null;
        this.value = null;
    }

    public NotFoundException(String entity, String field, Object value) {
        super(String.format("%s not found with field: %s and value: %s", entity, field, value));
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
    public Object getValue() {
        return value;
    }
}

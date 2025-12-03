package com.backend.api.invoice_manager.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final String table;
    private final String column;

    public DataIntegrityViolationException(String message) {
        super(message);
        this.table = null;
        this.column = null;
    }

    public DataIntegrityViolationException(String message, String table, String column) {
        super(message);
        this.table = table;
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }
}

package com.backend.api.invoice_manager.exceptions;

public class UnauthorizedActionException extends BusinessException {
    public UnauthorizedActionException(String message) {
        super(message);
    }

}

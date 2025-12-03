package com.backend.api.invoice_manager.exceptions;

public class ClientAlreadyExistsException extends AlreadyExistsException {
    public ClientAlreadyExistsException(String identifier) {
        super("Client with identifier " + identifier + " already exists.");
    }

}

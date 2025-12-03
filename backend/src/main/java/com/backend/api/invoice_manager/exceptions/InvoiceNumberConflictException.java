package com.backend.api.invoice_manager.exceptions;

public class InvoiceNumberConflictException extends AlreadyExistsException {
    public InvoiceNumberConflictException(String invoiceNumber) {
        super("Invoice number " + invoiceNumber + " already exists.");
    }

}

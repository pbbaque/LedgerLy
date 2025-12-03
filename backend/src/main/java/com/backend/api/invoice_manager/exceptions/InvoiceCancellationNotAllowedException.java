package com.backend.api.invoice_manager.exceptions;

public class InvoiceCancellationNotAllowedException extends BusinessException {
    public InvoiceCancellationNotAllowedException(String invoiceNumber) {
        super("Invoice with number " + invoiceNumber + " cannot be cancelled due to its current state.");
    }

}

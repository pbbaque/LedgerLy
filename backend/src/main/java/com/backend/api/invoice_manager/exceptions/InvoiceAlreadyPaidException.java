package com.backend.api.invoice_manager.exceptions;

public class InvoiceAlreadyPaidException extends BusinessException {
    public InvoiceAlreadyPaidException(String invoiceNumber) {
        super("Invoice with number " + invoiceNumber + " is already paid and cannot be modified.");
    }

}

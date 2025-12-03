package com.backend.api.invoice_manager.exceptions;

public class CompanyNotActiveException extends BusinessException {
    public CompanyNotActiveException(String companyName) {
        super("Company " + companyName + " is not active and cannot perform operations.");
    }

}

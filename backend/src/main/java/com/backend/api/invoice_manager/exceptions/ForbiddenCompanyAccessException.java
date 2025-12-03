package com.backend.api.invoice_manager.exceptions;

public class ForbiddenCompanyAccessException extends BusinessException {
    public ForbiddenCompanyAccessException(Long companyId) {
        super("Access to company with ID " + companyId + " is forbidden for this user.");
    }

}

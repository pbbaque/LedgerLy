package com.backend.api.invoice_manager.exceptions;

public class UserNotInCompanyException extends BusinessException {
    public UserNotInCompanyException(Long userId, Long companyId) {
        super("User with ID " + userId + " does not belong to company with ID " + companyId + ".");
    }
}

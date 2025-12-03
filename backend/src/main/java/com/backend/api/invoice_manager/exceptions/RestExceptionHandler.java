package com.backend.api.invoice_manager.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.backend.api.invoice_manager.responses.ApiResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, BindingResult bindingResult) {
        Map<String, List<String>> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors
                .computeIfAbsent(fieldError.getField(), _ -> new ArrayList<>())
                .add(fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, List<String>>> response = new ApiResponse<>(
            "Validation errors occurred.",
            errors,
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, List<String>> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors
                .computeIfAbsent(violation.getPropertyPath().toString(), _ -> new ArrayList<>())
                .add(violation.getMessage());
        }

        ApiResponse<Map<String, List<String>>> response = new ApiResponse<>(
            ex.getMessage(),
            errors,
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        String details = String.format("%s not found with %s: %s", ex.getEntity(), ex.getField(), ex.getValue());
        ApiResponse<String> response = new ApiResponse<>(
            ex.getMessage(),
            details,
            HttpStatus.NOT_FOUND.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String details = String.format("Integrity violation on table '%s', column '%s'. %s", 
                                       ex.getTable(), ex.getColumn(), ex.getMessage());
        ApiResponse<String> response = new ApiResponse<>(
            "Data integrity violation.",
            details,
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Malformed JSON request.",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<?> handleFileUploadException(FileUploadException ex) {
        String details = String.format("Error uploading file '%s'. Size: %d bytes. Message: %s", 
                                        ex.getFileName(), ex.getFileSize(), ex.getMessage());
        ApiResponse<String> response = new ApiResponse<>(
            "File upload error.",
            details,
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> handleAlreadyExistsException(AlreadyExistsException ex) {
        String details = String.format("%s already exists with %s: %s", 
                                        ex.getEntity(), ex.getField(), ex.getValue());
        if("email".equals(ex.getField())) {
            details = String.format("An account with the email '%s' already exists.", ex.getValue());
        }
        
        ApiResponse<String> response = new ApiResponse<>(
            null,
            details,
            HttpStatus.CONFLICT.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(ValidationException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Validation error occurred.",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<?> handleUnauthorizedActionException(UnauthorizedActionException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Unauthorized action.",
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ForbiddenCompanyAccessException.class)
    public ResponseEntity<?> handleForbiddenCompanyAccessException(ForbiddenCompanyAccessException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Access denied to company.",
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvoiceAlreadyPaidException.class)
    public ResponseEntity<?> handleInvoiceAlreadyPaidException(InvoiceAlreadyPaidException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Invoice already paid.",
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvoiceCancellationNotAllowedException.class)
    public ResponseEntity<?> handleInvoiceCancellationNotAllowedException(InvoiceCancellationNotAllowedException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Invoice cancellation not allowed.",
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvoiceNumberConflictException.class)
    public ResponseEntity<?> handleInvoiceNumberConflictException(InvoiceNumberConflictException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Invoice number conflict.",
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ClientAlreadyExistsException.class)
    public ResponseEntity<?> handleClientAlreadyExistsException(ClientAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Client already exists.",
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CompanyNotActiveException.class)
    public ResponseEntity<?> handleCompanyNotActiveException(CompanyNotActiveException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Company not active.",
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RoleAssignmentException.class)
    public ResponseEntity<?> handleRoleAssignmentException(RoleAssignmentException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "Role assignment error.",
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotInCompanyException.class)
    public ResponseEntity<?> handleUserNotInCompanyException(UserNotInCompanyException ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "User not in company.",
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Catch genérico para lo que no esté contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>(
            "An unexpected error occurred.",
            ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            false
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

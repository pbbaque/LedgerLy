package com.backend.api.invoice_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.UserOrchestrator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserOrchestrator userOrchestrator;

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        userOrchestrator.proccessForgotPassword(email);

        ApiResponse<String> response = new ApiResponse<>(
                "Password reset link sent to: " + email,
                null,
                HttpStatus.OK.value(),
                true
        );

        return ResponseEntity.ok(response);
    }
}

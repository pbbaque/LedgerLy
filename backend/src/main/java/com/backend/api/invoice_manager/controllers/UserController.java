package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.UserOrchestrator;
import com.backend.api.invoice_manager.validators.CreateUser;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserOrchestrator orchestrator;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> findAll() {
        List<User> users = orchestrator.findAll();
        ApiResponse<List<User>> response = new ApiResponse<>("Users retrieved successfully.", users,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        User user = orchestrator.findById(id);
        ApiResponse<User> response = new ApiResponse<>("User retrieved successfully.", user,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> getCurrentUser() {
        User user = orchestrator.getCurrentUser();
        ApiResponse<User> response = new ApiResponse<>("User retrieved successfully.", user,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        User user = orchestrator.findByEmail(email);
        ApiResponse<User> response = new ApiResponse<>("User retrieved successfully.", user,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> findByCompany(@PathVariable Long companyId) {
        List<User> users = orchestrator.findByEmployeeCompanyId(companyId);
        ApiResponse<List<User>> response = new ApiResponse<>("Users for company retrieved successfully.", users,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> searchUsers(@RequestParam(required = false) String term) {
        List<User> users = orchestrator.searchUsers(term);
        ApiResponse<List<User>> response = new ApiResponse<>("Search results.", users,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> searchUsersByCompany(@PathVariable Long companyId,
            @RequestParam(required = false) String term) {
        List<User> users = orchestrator.searchUsersByCompany(term, companyId);
        ApiResponse<List<User>> response = new ApiResponse<>("Search results for company.", users,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<?> create(@Validated(CreateUser.class) @RequestBody User user) {
        User created = orchestrator.create(user);
        ApiResponse<User> response = new ApiResponse<>("User created successfully.", created,
                HttpStatus.CREATED.value(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/for-company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> createUserForCompany(@PathVariable Long companyId, @Valid @RequestBody User user) {
        User created = orchestrator.createUserForCompany(user, companyId);
        ApiResponse<User> response = new ApiResponse<>("User created for company successfully.", created,
                HttpStatus.CREATED.value(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/forgot-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        orchestrator.proccessForgotPassword(email);
        ApiResponse<Void> response = new ApiResponse<>("Password reset link sent.", null,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY','USER')")
    public ResponseEntity<?> update(@Valid @RequestBody User user) {
        User updated = orchestrator.update(user);
        ApiResponse<User> response = new ApiResponse<>("User updated successfully.", updated,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User deleted = orchestrator.delete(id);
        ApiResponse<User> response = new ApiResponse<>("User deleted successfully.", deleted,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','COMPANY_ADMIN')")
    public ResponseEntity<?> findAllRoles() {
        List<Role> roles = orchestrator.findAllRoles();
        ApiResponse<List<Role>> response = new ApiResponse<>("Roles retrieved", roles,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok(response);
    }
}

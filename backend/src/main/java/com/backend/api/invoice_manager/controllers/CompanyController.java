package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.CompanyOrchestrator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyOrchestrator orchestrator;

    @GetMapping
    public ResponseEntity<?> findAll() {
        ApiResponse<List<Company>> response = new ApiResponse<>("Companies retrieved successfully.",
                orchestrator.findAll(), HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Company company = orchestrator.findById(id);
        ApiResponse<Company> response = new ApiResponse<>("Company with id: " + id + " found successfully.", company,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentCompany() {
        Company company = orchestrator.getCurrentCompany();
        ApiResponse<Company> response = new ApiResponse<>("Current company found successfully.", company,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/fiscal-number/{fiscalNumber}")
    public ResponseEntity<?> findByFiscalNumber(@PathVariable String fiscalNumber) {
        Company company = orchestrator.findByFiscalNumber(fiscalNumber);
        ApiResponse<Company> response = new ApiResponse<>(
                "Company with fiscal number: " + fiscalNumber + " found successfully.", company, HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        Company company = orchestrator.findByEmail(email);
        ApiResponse<Company> response = new ApiResponse<>("Company with email: " + email + " found successfully.",
                company, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<?> searchCompany(@RequestParam(required = false) String term) {
        List<Company> companies = orchestrator.searchCompanies(term);
        ApiResponse<List<Company>> response = new ApiResponse<>(
                "Companies found successfully.", companies, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Company company) {
        Company created = orchestrator.create(company);
        ApiResponse<Company> response = new ApiResponse<>(
                "Company with id: " + created.getId() + " created successfully.", created, HttpStatus.CREATED.value(),
                true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Company company) {
        Company updated = orchestrator.update(company);
        ApiResponse<Company> response = new ApiResponse<>(
                "Company with id: " + updated.getId() + " updated successfully.", updated, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Company deleted = orchestrator.delete(id);
        ApiResponse<Company> response = new ApiResponse<>(
                "Company with id: " + deleted.getId() + " deleted successfully.", deleted, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }
}

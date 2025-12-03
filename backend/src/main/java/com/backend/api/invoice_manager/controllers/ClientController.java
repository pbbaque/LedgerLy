package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.invoice_manager.dtos.TopClientDTO;
import com.backend.api.invoice_manager.entities.Client;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.ClientOrchestrator;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientOrchestrator orchestrator;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> findAll() {
        ApiResponse<List<Client>> response = new ApiResponse<>("Clients retrieved successfully.",
                orchestrator.findAll(), HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> findByCompanyId(@PathVariable Long companyId) {
        List<Client> clients = orchestrator.findByCompanyId(companyId);
        ApiResponse<List<Client>> response = new ApiResponse<>(
                "Clients for company " + companyId + " retrieved successfully.", clients, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Client client = orchestrator.findById(id);
        ApiResponse<Client> response = new ApiResponse<>("Client with id: " + id + " found successfully.", client,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> findByIdAndCompanyId(@PathVariable Long id, @PathVariable Long companyId) {
        Client client = orchestrator.findByIdAndCompanyId(id, companyId);
        ApiResponse<Client> response = new ApiResponse<>("Client with id: " + id + " found successfully.", client,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/email/{email}/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> findByEmailAndCompanyId(@PathVariable String email, @PathVariable Long companyId) {
        Client client = orchestrator.findByEmailAndCompanyId(email, companyId);
        ApiResponse<Client> response = new ApiResponse<>("Client with email: " + email + " found successfully.", client,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<?> searchClients(@RequestParam(required = false) String term) {
        List<Client> clients = orchestrator.searchClients(term);
        ApiResponse<List<Client>> response = new ApiResponse<>(
                "Clients found successfully.", clients, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/company/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> searchClientsByCompany(
            @PathVariable Long companyId,
            @RequestParam(required = false) String term) {
        List<Client> clients = orchestrator.searchClientsByCompany(term, companyId);
        ApiResponse<List<Client>> response = new ApiResponse<>(
                "Clients for company " + companyId + " found successfully.", clients, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopClients() {
        List<TopClientDTO> topClients = orchestrator.getTopClients();
        return ResponseEntity.ok(new ApiResponse<>("Top clients retrieved successfully.", topClients, 200, true));
    }

    @PostMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> create(@Valid @RequestBody Client client, @PathVariable Long companyId) {
        Client created = orchestrator.create(client, companyId);
        ApiResponse<Client> response = new ApiResponse<>(
                "Client with id: " + created.getId() + " created successfully.", created, HttpStatus.CREATED.value(),
                true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{companyId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY', 'USER')")
    public ResponseEntity<?> update(@Valid @RequestBody Client client, @PathVariable Long companyId) {
        Client updated = orchestrator.update(client, companyId);
        ApiResponse<Client> response = new ApiResponse<>(
                "Client with id: " + updated.getId() + " updated successfully.", updated, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Client deleted = orchestrator.delete(id);
        ApiResponse<Client> response = new ApiResponse<>(
                "Client with id: " + deleted.getId() + " deleted successfully.", deleted, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

}

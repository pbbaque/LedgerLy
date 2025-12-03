package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.backend.api.invoice_manager.dtos.MonthlyDataDTO;
import com.backend.api.invoice_manager.entities.Invoice;
import com.backend.api.invoice_manager.entities.InvoiceDetail;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.InvoiceOrchestrator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceOrchestrator orchestrator;

    @GetMapping
    public ResponseEntity<?> findAll() {
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices retrieved successfully.",
                orchestrator.findAll(),
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Invoice invoice = orchestrator.findById(id);
        ApiResponse<Invoice> response = new ApiResponse<>(
                "Invoice with id: " + id + " found successfully.",
                invoice,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> findDetailsByInvoiceId(@PathVariable Long id) {
        List<InvoiceDetail> details = orchestrator.findDetailsByInvoiceId(id);
        ApiResponse<List<InvoiceDetail>> response = new ApiResponse<>(
                "Invoice details for invoice id: " + id + " retrieved successfully.",
                details,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-client/{clientId}")
    public ResponseEntity<?> findByClientId(@PathVariable Long clientId) {
        List<Invoice> invoices = orchestrator.findByClientId(clientId);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices found for client with id: " + clientId,
                invoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-employee/{employeeId}")
    public ResponseEntity<?> findByEmployeeId(@PathVariable Long employeeId) {
        List<Invoice> invoices = orchestrator.findByEmployeeId(employeeId);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices found for employee with id: " + employeeId,
                invoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-company/{companyId}")
    public ResponseEntity<?> findByCompanyId(@PathVariable Long companyId) {
        List<Invoice> invoices = orchestrator.findByCompanyId(companyId);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices found for company with id: " + companyId,
                invoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search-by-date")
    public ResponseEntity<?> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        List<Invoice> invoices = orchestrator.findByDateRange(startDate, endDate);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices found in the date range.",
                invoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}/search-by-dates")
    public ResponseEntity<?> findByCompanyIdAndDateRange(
            @PathVariable Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String endDate) {
        List<Invoice> invoices = orchestrator.findByCompanyIdAndDateRange(companyId, startDate, endDate);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                "Invoices found for company " + companyId + " in the date range.",
                invoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total")
    public ResponseEntity<?> getTotalInvoiced() {
        double total = orchestrator.getTotalInvoiced();
        return ResponseEntity.ok(new ApiResponse<>("Total invoiced retrieved successfully.", total, 200, true));
    }

    @GetMapping("/this-month")
    public ResponseEntity<?> getInvoicesThisMonth() {
        long count = orchestrator.getInvoicesThisMonth();
        return ResponseEntity.ok(new ApiResponse<>("Invoices this month retrieved successfully.", count, 200, true));
    }

    @GetMapping("/today")
    public ResponseEntity<?> getInvoicesToday() {
        long count = orchestrator.getInvoicesToday();
        return ResponseEntity.ok(new ApiResponse<>("Invoices today retrieved successfully.", count, 200, true));
    }

    // Monthly chart data
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyInvoices() {
        List<MonthlyDataDTO> data = orchestrator.getMonthlyInvoices();
        return ResponseEntity.ok(new ApiResponse<>("Monthly invoices retrieved successfully.", data, 200, true));
    }

    // Latest invoices
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestInvoices() {
        List<Invoice> latest = orchestrator.getLatestInvoices();
        return ResponseEntity.ok(new ApiResponse<>("Latest invoices retrieved successfully.", latest, 200, true));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> create(@Valid @RequestBody Invoice invoice) {
        Invoice createdInvoice = orchestrator.create(invoice);
        ApiResponse<Invoice> response = new ApiResponse<>(
                "Invoice created successfully.",
                createdInvoice,
                HttpStatus.CREATED.value(),
                true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> createBulk(@Valid @RequestBody List<Invoice> invoices) {
        List<Invoice> createdInvoices = orchestrator.createAll(invoices);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                createdInvoices.size() + " invoices created successfully.",
                createdInvoices,
                HttpStatus.CREATED.value(),
                true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> update(@Valid @RequestBody Invoice invoice) {
        Invoice updatedInvoice = orchestrator.update(invoice);
        ApiResponse<Invoice> response = new ApiResponse<>(
                "Invoice with id: " + updatedInvoice.getId() + " updated successfully.",
                updatedInvoice,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Invoice deletedInvoice = orchestrator.delete(id);
        ApiResponse<Invoice> response = new ApiResponse<>(
                "Invoice with id: " + deletedInvoice.getId() + " deleted successfully.",
                deletedInvoice,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/bulk")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'SUPER_ADMIN_COMPANY', 'ADMIN_COMPANY')")
    public ResponseEntity<?> deleteBulk(@RequestBody List<Long> ids) {
        List<Invoice> deletedInvoices = orchestrator.deleteAll(ids);
        ApiResponse<List<Invoice>> response = new ApiResponse<>(
                deletedInvoices.size() + " invoices deleted successfully.",
                deletedInvoices,
                HttpStatus.OK.value(),
                true);
        return ResponseEntity.ok(response);
    }
}

package com.backend.api.invoice_manager.services.orchestrators;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.dtos.MonthlyDataDTO;
import com.backend.api.invoice_manager.entities.Invoice;
import com.backend.api.invoice_manager.entities.InvoiceDetail;
import com.backend.api.invoice_manager.entities.InvoiceDetailId;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.services.client.ClientService;
import com.backend.api.invoice_manager.services.company.CompanyService;
import com.backend.api.invoice_manager.services.employee.EmployeeService;
import com.backend.api.invoice_manager.services.invoice.InvoiceService;
import com.backend.api.invoice_manager.services.invoiceDetail.InvoiceDetailService;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;
import com.backend.api.invoice_manager.services.product.ProductService;

@Component
public class InvoiceOrchestrator {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceDetailService invoiceDetailService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PermissionOrchestrator permissionOrchestrator;

    public List<Invoice> findAll() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.findByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.findAll();
    }

    public Invoice findById(Long id) {
        Invoice invoice = invoiceService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(invoice.getCompany().getId());
        return invoice;
    }

    public List<Invoice> findByClientId(Long clientId) {
        Long companyId = clientService.findById(clientId).getCompany().getId();
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return invoiceService.findByClientId(clientId);
    }

    public List<Invoice> findByEmployeeId(Long employeeId) {
        Long companyId = employeeService.findById(employeeId).getCompany().getId();
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return invoiceService.findByEmployeeId(employeeId);
    }

    public List<Invoice> findByCompanyId(Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return invoiceService.findByCompanyId(companyId);
    }

    public List<InvoiceDetail> findDetailsByInvoiceId(Long invoiceId) {
        Invoice invoice = invoiceService.findById(invoiceId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(invoice.getCompany().getId());
        return invoiceDetailService.findDetailByInvoiceId(invoiceId);
    }

    public List<Invoice> findByDateRange(String startDateStr, String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        permissionOrchestrator.requireSuperAdminOrAdmin();
        List<Invoice> invoices = invoiceService.findByDateRange(startDate, endDate);

        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found in the specified date range.");
        }

        return invoices;
    }

    public List<Invoice> findByCompanyIdAndDateRange(Long companyId, String startDateStr, String endDateStr) {
        LocalDate startDate;
        LocalDate endDate;

        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date.");
        }

        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        List<Invoice> invoices = invoiceService.findByCompanyIdAndDateRange(companyId, startDate, endDate);

        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for company " + companyId + " in the specified date range.");
        }

        return invoices;
    }

    public double getTotalInvoiced() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.getTotalInvoicedByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.getTotalInvoiced();
    }

    public long getInvoicesThisMonth() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.getInvoicesThisMonthByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.getInvoicesThisMonth();
    }

    public long getInvoicesToday() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.getInvoicesTodayByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.getInvoicesToday();
    }

    public List<MonthlyDataDTO> getMonthlyInvoices() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.getMonthlyInvoicesByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.getMonthlyInvoices();
    }

    public List<Invoice> getLatestInvoices() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return invoiceService.getLatestInvoicesByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return invoiceService.getLatestInvoices();
    }

    public Invoice create(Invoice invoice) {
        // Validación de permisos
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(invoice.getCompany().getId());

        // Asegurarse de que se cree un nuevo registro
        invoice.setId(null);

        // Recuperar entidades completas
        invoice.setEmployee(employeeService.findById(invoice.getEmployee().getId()));
        invoice.setClient(clientService.findById(invoice.getClient().getId()));
        invoice.setCompany(companyService.findById(invoice.getCompany().getId()));

        // Asignar el Invoice y los Products a cada detalle
        if (invoice.getDetails() != null) {
            for (InvoiceDetail detail : invoice.getDetails()) {
                detail.setInvoice(invoice); // @MapsId("invoiceId") asigna automáticamente invoiceId
                detail.setProduct(productService.findById(detail.getProduct().getId())); // @MapsId("productId")

                // Inicializar el EmbeddedId para evitar null
                detail.setId(new InvoiceDetailId());
            }
        }

        // Guardar invoice y cascada a detalles
        Invoice saved = invoiceService.save(invoice);

        return saved;
    }

    public List<Invoice> createAll(List<Invoice> invoices) {
        if (invoices == null || invoices.isEmpty()) {
            throw new NotFoundException("No invoices provided for creation.");
        }

        for (Invoice invoice : invoices) {
            permissionOrchestrator.requireCompanyOwnershipOrAdmin(invoice.getCompany().getId());

            invoice.setEmployee(employeeService.findById(invoice.getEmployee().getId()));
            invoice.setClient(clientService.findById(invoice.getClient().getId()));
            invoice.setCompany(companyService.findById(invoice.getCompany().getId()));

            Invoice saved = invoiceService.save(invoice);
            if (invoice.getDetails() != null) {
                for (InvoiceDetail detail : invoice.getDetails()) {
                    detail.setInvoice(saved);
                    invoiceDetailService.save(detail);
                }
            }
        }

        return invoices;
    }

    public Invoice update(Invoice invoice) {
        // 1. Obtener la factura existente y validar permisos
        Invoice existing = invoiceService.findById(invoice.getId());
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getCompany().getId());

        // 2. Recuperar entidades completas
        invoice.setEmployee(employeeService.findById(invoice.getEmployee().getId()));
        invoice.setClient(clientService.findById(invoice.getClient().getId()));
        invoice.setCompany(companyService.findById(invoice.getCompany().getId()));

        // 3. Actualizar detalles: eliminar los existentes y preparar los nuevos
        if (invoice.getDetails() != null) {
            // Obtener y eliminar detalles existentes
            List<InvoiceDetail> existingDetails = invoiceDetailService.findDetailByInvoiceId(invoice.getId());
            for (InvoiceDetail detail : existingDetails) {
                invoiceDetailService.delete(detail.getId());
            }

            // Asignar Invoice y Product a cada detalle y preparar EmbeddedId
            for (InvoiceDetail detail : invoice.getDetails()) {
                detail.setInvoice(invoice); // @MapsId("invoiceId")
                detail.setProduct(productService.findById(detail.getProduct().getId())); // @MapsId("productId")
                if (detail.getId() == null) {
                    detail.setId(new InvoiceDetailId());
                }
            }
        }

        // 4. Guardar la factura con cascada a detalles
        Invoice updated = invoiceService.update(invoice);

        return updated;
    }

    public Invoice delete(Long id) {
        Invoice existing = invoiceService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getCompany().getId());

        List<InvoiceDetail> details = invoiceDetailService.findDetailByInvoiceId(existing.getId());
        for (InvoiceDetail detail : details) {
            invoiceDetailService.delete(detail.getId());
        }

        return invoiceService.delete(id);
    }

    public List<Invoice> deleteAll(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new NotFoundException("No invoice IDs provided for deletion.");
        }

        List<Invoice> invoices = invoiceService.deleteAll(ids);
        for (Invoice invoice : invoices) {
            permissionOrchestrator.requireCompanyOwnershipOrAdmin(invoice.getCompany().getId());
        }

        return invoices;
    }
}

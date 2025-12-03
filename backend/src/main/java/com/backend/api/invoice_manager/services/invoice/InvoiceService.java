package com.backend.api.invoice_manager.services.invoice;

import java.time.LocalDate;
import java.util.List;

import com.backend.api.invoice_manager.dtos.MonthlyDataDTO;
import com.backend.api.invoice_manager.entities.Invoice;

public interface InvoiceService {

    List<Invoice> findAll();

    Invoice findById(Long id);

    List<Invoice> findByClientId(Long clientId);

    List<Invoice> findByEmployeeId(Long employeeId);

    List<Invoice> findByCompanyId(Long companyId);

    List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate);

    List<Invoice> findByCompanyIdAndDateRange(Long companyId, LocalDate startDate, LocalDate endDate);

    double getTotalInvoiced();

    double getTotalInvoicedByCompanyId(Long companyId);

    long getInvoicesThisMonth();

    long getInvoicesThisMonthByCompanyId(Long companyId);

    long getInvoicesToday();
    
    long getInvoicesTodayByCompanyId(Long companyId);

    List<MonthlyDataDTO> getMonthlyInvoices();

    List<MonthlyDataDTO> getMonthlyInvoicesByCompanyId(Long companyId);

    List<Invoice> getLatestInvoices();

    List<Invoice> getLatestInvoicesByCompanyId(Long companyId);

    Invoice save(Invoice invoice);

    Invoice update(Invoice invoice);

    Invoice delete(Long id);

    List<Invoice> deleteAll(List<Long> ids);

}

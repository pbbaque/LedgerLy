package com.backend.api.invoice_manager.services.invoice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.dtos.MonthlyDataDTO;
import com.backend.api.invoice_manager.entities.Invoice;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.InvoiceRepository;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = (List<Invoice>) repository.findAll();
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found in the database.");
        }
        return invoices;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Invoice> findByClientId(Long clientId) {
        List<Invoice> invoices = repository.findByClientId(clientId);
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for client ID: " + clientId);
        }
        return invoices;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Invoice> findByEmployeeId(Long employeeId) {
        List<Invoice> invoices = repository.findByEmployeeId(employeeId);
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for employee ID: " + employeeId);
        }
        return invoices;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Invoice> findByCompanyId(Long companyId) {
        List<Invoice> invoices = repository.findByCompanyId(companyId);
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for company ID: " + companyId);
        }
        return invoices;
    }

    @Transactional(readOnly = true)
    @Override
    public Invoice findById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Invoice", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = repository.findByDateBetween(startDate, endDate);
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found in the provided date range.");
        }
        return invoices;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Invoice> findByCompanyIdAndDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = repository.findByCompanyIdAndDateBetween(companyId, startDate, endDate);
        if (invoices.isEmpty()) {
            throw new NotFoundException("No invoices found for company " + companyId + " in the provided date range.");
        }
        return invoices;
    }

    @Override
    @Transactional(readOnly = true)
    public double getTotalInvoiced() {
        return repository.sumAllInvoices();
    }

    @Override
    @Transactional(readOnly = true)
    public double getTotalInvoicedByCompanyId(Long companyId) {
        return repository.sumAllInvoicesByCompanyId(companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getInvoicesThisMonth() {
        YearMonth now = YearMonth.now();
        return repository.countByMonth(now.getMonthValue(), now.getYear());
    }

    @Override
    @Transactional(readOnly = true)
    public long getInvoicesThisMonthByCompanyId(Long companyId) {
        YearMonth now = YearMonth.now();
        return repository.countByMonthAndCompanyId(now.getMonthValue(), now.getYear(), companyId); 
    }

    @Override
    @Transactional(readOnly = true)
    public long getInvoicesToday() {
        LocalDate today = LocalDate.now();
        return repository.countByDate(today);
    }

    @Override
    @Transactional(readOnly = true)
    public long getInvoicesTodayByCompanyId(Long companyId) {
        LocalDate today = LocalDate.now();
        return repository.countByDateAndCompanyId(today, companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyDataDTO> getMonthlyInvoices() {
        return repository.findMonthlyTotals();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlyDataDTO> getMonthlyInvoicesByCompanyId(Long companyId) {
        return repository.findMonthlyTotalsByCompanyId(companyId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getLatestInvoices() {
        return repository.findTop5ByOrderByDateDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getLatestInvoicesByCompanyId(Long companyId) {
        return repository.findTop5ByCompanyIdOrderByDateDesc(companyId);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public Invoice update(Invoice invoice) {
        return repository.save(invoice);
    }

    @Override
    public Invoice delete(Long id) {
        Invoice deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

    @Override
    public List<Invoice> deleteAll(List<Long> ids) {
        List<Invoice> deletedInvoices = (List<Invoice>) repository.findAllById(ids);
        if (deletedInvoices.isEmpty()) {
            throw new NotFoundException("No invoices found for the provided IDs.");
        }
        repository.deleteAll(deletedInvoices);
        return deletedInvoices;
    }

}

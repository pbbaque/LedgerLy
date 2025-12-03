package com.backend.api.invoice_manager.services.invoiceDetail;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.entities.InvoiceDetail;
import com.backend.api.invoice_manager.entities.InvoiceDetailId;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.InvoiceDetailRepository;

@Service
@Transactional
public class InvoiceDetailServiceImpl implements InvoiceDetailService {

    @Autowired
    private InvoiceDetailRepository repository;

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(InvoiceDetailId id) {
        return repository.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceDetail> findDetailByInvoiceId(Long invoiceId) {
        List<InvoiceDetail> invoiceDetails = repository.findByIdInvoiceId(invoiceId);
        if (invoiceDetails.isEmpty()) {
            throw new NotFoundException("No invoice details found for invoice ID: " + invoiceId);
        }
        return invoiceDetails;
    }

    @Transactional(readOnly = true)
    @Override
    public InvoiceDetail findById(InvoiceDetailId id) {
        return repository.findById(id)
            .orElseThrow(() ->  new NotFoundException("InvoiceDetail", "id", id));
    }

    @Override
    public InvoiceDetail save(InvoiceDetail invoiceDetail) {
        if (existsById(invoiceDetail.getId())) {
            throw new AlreadyExistsException("InvoiceDetail with ID " + invoiceDetail.getId() + " already exists.");
        }
        return repository.save(invoiceDetail);
    }

    @Override
    public InvoiceDetail update(InvoiceDetail invoiceDetail) {
        InvoiceDetail updated = repository.findById(invoiceDetail.getId())
            .orElseThrow(() -> new NotFoundException("InvoiceDetail", "id", invoiceDetail.getId()));
        updated.setProduct(invoiceDetail.getProduct());
        updated.setAmount(invoiceDetail.getAmount());
        updated.setQuantity(invoiceDetail.getQuantity());
        updated.setUnitPrice(invoiceDetail.getUnitPrice());
        return repository.save(updated);
    }

    @Override
    public InvoiceDetail delete(InvoiceDetailId id) {
        InvoiceDetail deleted = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("InvoiceDetail", "id", id));
        repository.delete(deleted);
        return deleted;
    }
}

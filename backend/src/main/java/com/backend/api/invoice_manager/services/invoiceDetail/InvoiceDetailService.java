package com.backend.api.invoice_manager.services.invoiceDetail;

import java.util.List;

import com.backend.api.invoice_manager.entities.InvoiceDetail;
import com.backend.api.invoice_manager.entities.InvoiceDetailId;

public interface InvoiceDetailService {

    boolean existsById(InvoiceDetailId id);

    List<InvoiceDetail> findDetailByInvoiceId(Long invoiceId);

    InvoiceDetail findById(InvoiceDetailId id);

    InvoiceDetail save(InvoiceDetail invoiceDetail);

    InvoiceDetail update(InvoiceDetail invoiceDetail);

    InvoiceDetail delete(InvoiceDetailId id);
}

package com.backend.api.invoice_manager.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InvoiceDetailId implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "product_id")
    private Long productId;

    public InvoiceDetailId() {}

    public InvoiceDetailId(Long invoiceId, Long productId) {
        this.invoiceId = invoiceId;
        this.productId = productId;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceDetailId)) return false;

        InvoiceDetailId that = (InvoiceDetailId) o;

        if (!invoiceId.equals(that.invoiceId)) return false;
        return productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        int result = invoiceId.hashCode();
        result = 31 * result + productId.hashCode();
        return result;
    }
}

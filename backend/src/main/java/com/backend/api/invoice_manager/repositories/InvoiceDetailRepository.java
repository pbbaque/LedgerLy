package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.entities.InvoiceDetail;
import com.backend.api.invoice_manager.entities.InvoiceDetailId;

public interface InvoiceDetailRepository extends CrudRepository<InvoiceDetail, InvoiceDetailId> {

    @Override
    @EntityGraph(attributePaths = { "invoice", "product" })
    @NonNull
    List<InvoiceDetail> findAll();

    @Override
    @EntityGraph(attributePaths = { "invoice", "product" })
    @NonNull
    Optional<InvoiceDetail> findById(@NonNull InvoiceDetailId id);

    @EntityGraph(attributePaths = { "invoice", "product" })
    List<InvoiceDetail> findByIdInvoiceId(Long invoiceId);

    @EntityGraph(attributePaths = { "invoice", "product" })
    List<InvoiceDetail> deleteByIdInvoiceId(Long invoiceId);

    @Query("""
                SELECT id FROM InvoiceDetail id
                LEFT JOIN FETCH id.invoice i
                LEFT JOIN FETCH id.product p
                WHERE (:term IS NULL
                    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(i.description) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    List<InvoiceDetail> searchInvoiceDetails(@Param("term") String term);

    @Query("""
                SELECT id FROM InvoiceDetail id
                LEFT JOIN FETCH id.invoice i
                LEFT JOIN FETCH i.company c
                LEFT JOIN FETCH id.product p
                WHERE c.id = :companyId
                  AND (:term IS NULL
                    OR LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(i.description) LIKE LOWER(CONCAT('%', :term, '%'))
                  )
            """)
    List<InvoiceDetail> searchInvoiceDetailsByCompany(@Param("term") String term, @Param("companyId") Long companyId);

}

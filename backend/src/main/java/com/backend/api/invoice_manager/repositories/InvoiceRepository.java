package com.backend.api.invoice_manager.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.dtos.MonthlyDataDTO;
import com.backend.api.invoice_manager.entities.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Override
    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    @NonNull
    List<Invoice> findAll();

    @Override
    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    @NonNull
    Optional<Invoice> findById(@NonNull Long id);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByClientId(Long clientId);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByEmployeeId(Long employeeId);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByCompanyId(Long companyId);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByDescriptionContainingIgnoreCase(String description);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByDateBetween(LocalDate start, LocalDate end);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findByCompanyIdAndDateBetween(Long companyId, LocalDate startDate, LocalDate endDate);

    @Query("""
                SELECT i FROM Invoice i
                LEFT JOIN FETCH i.employee e
                LEFT JOIN FETCH i.client c
                LEFT JOIN FETCH i.company co
                LEFT JOIN FETCH i.details d
                LEFT JOIN FETCH d.product p
                WHERE (:term IS NULL
                    OR LOWER(i.description) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.lastname) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(co.name) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    List<Invoice> searchInvoices(@Param("term") String term);

    @Query("""
                SELECT i FROM Invoice i
                LEFT JOIN FETCH i.employee e
                LEFT JOIN FETCH i.client c
                LEFT JOIN FETCH i.company co
                LEFT JOIN FETCH i.details d
                LEFT JOIN FETCH d.product p
                WHERE co.id = :companyId
                  AND (:term IS NULL
                    OR LOWER(i.description) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.lastname) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    List<Invoice> searchInvoicesByCompany(@Param("term") String term, @Param("companyId") Long companyId);

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM Invoice i")
    double sumAllInvoices();

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.company.id = :companyId")
    double sumAllInvoicesByCompanyId(@Param("companyId") Long companyId);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE MONTH(i.date) = :month AND YEAR(i.date) = :year")
    long countByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE MONTH(i.date) = :month AND i.company.id = :companyId AND YEAR(i.date) = :year")
    long countByMonthAndCompanyId(@Param("month") int month, @Param("year") int year, @Param("companyId") Long companyId);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE DATE(i.date) = :date")
    long countByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE DATE(i.date) = :date AND i.company.id = :companyId")
    long countByDateAndCompanyId(@Param("date") LocalDate date, @Param("companyId") Long companyId);

    @Query(value = """
                SELECT MONTHNAME(date) AS monthName,
                       SUM(total) AS total
                FROM invoices
                GROUP BY MONTH(date), MONTHNAME(date)
                ORDER BY MONTH(date)
            """, nativeQuery = true)
    List<MonthlyDataDTO> findMonthlyTotals();

    @Query(value = """
                SELECT MONTHNAME(date) AS monthName,
                       SUM(total) AS total
                FROM invoices
                WHERE company_id = :companyId
                GROUP BY MONTH(date), MONTHNAME(date)
                ORDER BY MONTH(date)
            """, nativeQuery = true)
    List<MonthlyDataDTO> findMonthlyTotalsByCompanyId(@Param("companyId") Long companyId);

    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findTop5ByOrderByDateDesc();
  
    @EntityGraph(attributePaths = { "employee", "client", "company", "details", "details.product" })
    List<Invoice> findTop5ByCompanyIdOrderByDateDesc(Long companyId);

}

package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.entities.Company;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    @Override
    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address
           """)
    @NonNull
    List<Company> findAll();

    @Override
    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address
           WHERE c.id = :id
           """)
    @NonNull
    Optional<Company> findById(@NonNull Long id);

    boolean existsByFiscalNumber(String fiscalNumber);

    boolean existsByEmail(String email);

    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address
           WHERE c.fiscalNumber = :fiscalNumber
           """)
    Optional<Company> findByFiscalNumber(String fiscalNumber);

    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address
           WHERE c.email = :email
           """)
    Optional<Company> findByEmail(String email);

    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address a
           WHERE (:term IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(c.fiscalNumber) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(a.street) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :term, '%')))
              OR (:term IS NULL OR LOWER(a.country) LIKE LOWER(CONCAT('%', :term, '%')))
           """)
    List<Company> searchCompanies(@Param("term") String term);

    @Query("""
           SELECT DISTINCT c FROM Company c
           LEFT JOIN FETCH c.employees
           LEFT JOIN FETCH c.address a
           WHERE c.id = :companyId
             AND (
                  (:term IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.fiscalNumber) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(a.street) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(a.country) LIKE LOWER(CONCAT('%', :term, '%')))
             )
           """)
    List<Company> searchCompanyById(@Param("term") String term, @Param("companyId") Long companyId);
}

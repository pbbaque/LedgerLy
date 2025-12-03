package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.dtos.TopClientDTO;
import com.backend.api.invoice_manager.entities.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {

    @Override
    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            """)
    @NonNull
    List<Client> findAll();

    @Override
    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.id = :id
            """)
    @NonNull
    Optional<Client> findById(@NonNull Long id);

    boolean existsByEmailAndCompanyId(String email, Long companyId);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.id = :id AND c.company.id = :companyId
            """)
    Optional<Client> findByIdAndCompanyId(Long id, Long companyId);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.email = :email
            """)
    Optional<Client> findByEmail(String email);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.email = :email AND c.company.id = :companyId
            """)
    Optional<Client> findByEmailAndCompanyId(String email, Long companyId);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.company.id = :companyId
            """)
    List<Client> findByCompanyId(Long companyId);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE (:term IS NULL OR LOWER(CONCAT(c.name, ' ', c.lastname)) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.address.street) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.address.city) LIKE LOWER(CONCAT('%', :term, '%')))
               OR (:term IS NULL OR LOWER(c.address.country) LIKE LOWER(CONCAT('%', :term, '%')))
            """)
    List<Client> searchClients(@Param("term") String term);

    @Query("""
            SELECT DISTINCT c FROM Client c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH c.invoices
            LEFT JOIN FETCH c.company co
            LEFT JOIN FETCH co.address
            WHERE c.company.id = :companyId
              AND (
                LOWER(CONCAT(c.name, ' ', c.lastname)) LIKE LOWER(CONCAT('%', :term, '%'))
                OR (:term IS NULL OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :term, '%')))
                OR (:term IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :term, '%')))
                OR (:term IS NULL OR LOWER(c.address.street) LIKE LOWER(CONCAT('%', :term, '%')))
                OR (:term IS NULL OR LOWER(c.address.city) LIKE LOWER(CONCAT('%', :term, '%')))
                OR (:term IS NULL OR LOWER(c.address.country) LIKE LOWER(CONCAT('%', :term, '%')))
              )
            """)
    List<Client> searchClientsByCompany(@Param("term") String term, @Param("companyId") Long companyId);

    @Query(value = """
                SELECT c.name,
                       c.lastname,
                       SUM(i.total) AS total
                FROM clients c
                JOIN invoices i ON c.id = i.client_id
                GROUP BY c.id, c.name, c.lastname
                ORDER BY total DESC
                LIMIT 5
            """, nativeQuery = true)
    List<TopClientDTO> findTopClients();
    
@Query(value = """
        SELECT c.name,
               c.lastname,
               SUM(i.total) AS total
        FROM clients c
        JOIN invoices i ON c.id = i.client_id
        WHERE c.company_id = :companyId
        GROUP BY c.id, c.name, c.lastname
        ORDER BY total DESC
        LIMIT 5
    """, nativeQuery = true)
    List<TopClientDTO> findTopClientsByCompanyId(@Param("companyId")Long companyId);

}

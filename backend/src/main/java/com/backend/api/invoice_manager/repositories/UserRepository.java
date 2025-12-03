package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    @EntityGraph(attributePaths = { "employee", "employee.company", "employee.company.address", "roles" })
    @NonNull
    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.employee e
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            LEFT JOIN FETCH u.roles
            """)
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = { "employee", "employee.company", "employee.company.address", "roles" })
    @NonNull
    Optional<User> findById(@NonNull Long id);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = { "employee", "employee.company", "employee.company.address", "roles" })
    Optional<User> findByEmail(String email);

    List<User> findByEmployeeCompanyId(Long companyId);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.employee e
                LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH e.company c
                LEFT JOIN FETCH c.address a
                WHERE (:term IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')))
            """)
    List<User> searchUsers(@Param("term") String term);

    @Query("""
                SELECT u FROM User u
                LEFT JOIN FETCH u.employee e
                LEFT JOIN FETCH u.roles r
                LEFT JOIN FETCH e.company c
                LEFT JOIN FETCH c.address a
                WHERE c.id = :companyId
                  AND (
                      (:term IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(e.lastname) LIKE LOWER(CONCAT('%', :term, '%')))
                   OR (:term IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')))
                  )
            """)
    List<User> searchUsersByCompany(@Param("term") String term, @Param("companyId") Long companyId);
}

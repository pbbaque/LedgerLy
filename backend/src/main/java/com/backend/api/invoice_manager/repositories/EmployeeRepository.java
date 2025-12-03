package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.entities.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Override
    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            """)
    @NonNull
    List<Employee> findAll();

    @Override
    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.id = :id
            """)
    @NonNull
    Optional<Employee> findById(@NonNull Long id);

    boolean existsByEmail(String email);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.email = :email
            """)
    Optional<Employee> findByEmail(String email);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.user.id = :userId
            """)
    Optional<Employee> findByUserId(Long userId);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.user.id = :userId AND e.company.id = :companyId
            """)
    Optional<Employee> findByUserIdAndCompanyId(Long userId, Long companyId);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.id = :id AND e.company.id = :companyId
            """)
    Optional<Employee> findByIdAndCompanyId(Long id, Long companyId);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.name = :name AND e.lastname = :lastname
            """)
    List<Employee> findByNameAndLastname(String name, String lastname);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.name = :name AND e.lastname = :lastname AND e.company.id = :companyId
            """)
    List<Employee> findByNameAndLastnameAndCompanyId(String name, String lastname, Long companyId);

    @Query("""
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.user u
            LEFT JOIN FETCH u.roles
            LEFT JOIN FETCH e.company c
            LEFT JOIN FETCH c.address
            WHERE e.company.id = :companyId
            """)
    List<Employee> findByCompanyId(Long companyId);

    @Query("""
                SELECT e FROM Employee e
                LEFT JOIN FETCH e.user u
                LEFT JOIN FETCH u.roles
                LEFT JOIN FETCH e.company c
                LEFT JOIN FETCH c.address
                WHERE (
                    :term IS NULL
                    OR LOWER(CONCAT(e.name, ' ', e.lastname)) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.phone) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.position) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    List<Employee> searchEmployees(@Param("term") String term);

    @Query("""
                SELECT e FROM Employee e
                LEFT JOIN FETCH e.user u
                LEFT JOIN FETCH u.roles
                LEFT JOIN FETCH e.company c
                LEFT JOIN FETCH c.address
                WHERE e.company.id = :companyId
                AND (
                    :term IS NULL
                    OR LOWER(CONCAT(e.name, ' ', e.lastname)) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.phone) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(e.position) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(u.username) LIKE LOWER(CONCAT('%', :term, '%'))
                    OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))
                )
            """)
    List<Employee> searchEmployeesByCompany(@Param("term") String term, @Param("companyId") Long companyId);
}
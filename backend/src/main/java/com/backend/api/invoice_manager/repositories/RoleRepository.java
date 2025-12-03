package com.backend.api.invoice_manager.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.backend.api.invoice_manager.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    boolean existsByName(String name);
    Optional<Role> findByName(String name);

}

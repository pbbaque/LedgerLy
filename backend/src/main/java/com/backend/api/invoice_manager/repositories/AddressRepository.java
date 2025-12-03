package com.backend.api.invoice_manager.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.backend.api.invoice_manager.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {

    boolean existsByStreetAndCityAndNumber(String street, String city, String number);

    Optional<Address> findByStreetAndCityAndNumber(String street, String city, String number);

}

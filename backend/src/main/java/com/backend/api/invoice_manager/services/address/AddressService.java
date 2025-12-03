package com.backend.api.invoice_manager.services.address;

import java.util.List;

import com.backend.api.invoice_manager.entities.Address;

public interface AddressService {
    boolean existsByStreetAndCityAndNumber(String street, String city, String number);

    List <Address> findAll();

    Address findById(Long id);

    Address findByStreetAndCityAndNumber(String street, String city, String number);

    Address save(Address address);

    Address update(Address address);

    Address delete(Long id);

}

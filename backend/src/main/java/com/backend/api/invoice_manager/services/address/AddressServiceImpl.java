package com.backend.api.invoice_manager.services.address;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.entities.Address;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.AddressRepository;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository repository;

    @Transactional(readOnly = true)
    @Override
    public boolean existsByStreetAndCityAndNumber(String street, String city, String number) {
        return repository.existsByStreetAndCityAndNumber(street, city, number);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Address> findAll() {
        List<Address> addresses = (List<Address>) repository.findAll();
        if (addresses.isEmpty()) {
            throw new NotFoundException("No addresses found in the database.");
        }
        return addresses;
    }

    @Transactional(readOnly = true)
    @Override
    public Address findById(Long id) {
        return repository.findById(id).orElseThrow(
            () -> new NotFoundException("Address", "id", id));
    }

    @Transactional(readOnly = true)
    @Override
    public Address findByStreetAndCityAndNumber(String street, String city, String number) {
        return repository.findByStreetAndCityAndNumber(street, city, number).orElseThrow(
            () -> new NotFoundException("Address", "Street, City and Number", 
                street + ", " + city + ", " + number));
    }

    @Override
    public Address save(Address address) {
        if(existsByStreetAndCityAndNumber(address.getStreet(), address.getCity(), address.getNumber())) 
            throw new AlreadyExistsException("Address", "Street, City and Number", 
                address.getStreet() + ", " + address.getCity() + ", " + address.getNumber());
        return repository.save(address);
    }

    @Override
    public Address update(Address address) {
        Address updated = findById(address.getId());
        updated.setCountry(address.getCountry());
        updated.setStreet(address.getStreet());
        updated.setCity(address.getCity());
        updated.setNumber(address.getNumber());
        return repository.save(updated);
    }

    @Override
    public Address delete(Long id) {
        Address deleted = findById(id);
        repository.delete(deleted);
        return deleted;
    }

}

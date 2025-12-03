package com.backend.api.invoice_manager.services.orchestrators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.dtos.TopClientDTO;
import com.backend.api.invoice_manager.entities.Address;
import com.backend.api.invoice_manager.entities.Client;
import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.services.address.AddressService;
import com.backend.api.invoice_manager.services.client.ClientService;
import com.backend.api.invoice_manager.services.company.CompanyService;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;

@Component
public class ClientOrchestrator {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PermissionOrchestrator permissionOrchestrator;

    @Autowired
    private AddressService addressService;

    public List<Client> findAll() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin()) {
            Company currentCompany = permissionOrchestrator.getCurrentCompany();
            return clientService.findByCompanyId(currentCompany.getId());
        }
        return clientService.findAll();
    }

    public List<Client> findByCompanyId(Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return clientService.findByCompanyId(companyId);
    }

    public Client findById(Long id) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin()) {
            Company currentCompany = permissionOrchestrator.getCurrentCompany();
            return findByIdAndCompanyId(id, currentCompany.getId());
        }
        return clientService.findById(id);
    }

    public Client findByIdAndCompanyId(Long id, Long companyId) {
        Client existing = clientService.findByIdAndCompanyId(id, companyId);
        permissionOrchestrator.requireCompanyEmployee(companyId);
        return existing;
    }

    public Client findByEmailAndCompanyId(String email, Long companyId) {
        permissionOrchestrator.requireCompanyEmployee(companyId);
        return clientService.findByEmailAndCompanyId(email, companyId);
    }

    public List<Client> searchClients(String term) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin()) {
            Company currentCompany = permissionOrchestrator.getCurrentCompany();
            return clientService.searchClientsByCompany(term, currentCompany.getId());
        }
        return clientService.searchClients(term);
    }

    public List<Client> searchClientsByCompany(String term, Long companyId) {
        permissionOrchestrator.requireCompanyEmployee(companyId);
        return clientService.searchClientsByCompany(term, companyId);
    }

    public List<TopClientDTO> getTopClients() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin()) {
            Company currentCompany = permissionOrchestrator.getCurrentCompany();
            return clientService.getTopClientsByCompanyId(currentCompany.getId());
        }
        return clientService.getTopClients();
    }

    public Client create(Client client, Long companyId) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin()) {
            Company currentCompany = permissionOrchestrator.getCurrentCompany();
            client.setCompany(currentCompany);
        } else {
            client.setCompany(companyService.findById(companyId));
        }
        if (client.getAddress() == null)
            throw new NotFoundException("Address can not be null.");

        if (client.getCompany() == null)
            throw new NotFoundException("Company can not be null.");
        clientService.existsByEmailAndCompanyId(client.getEmail(), client.getCompany().getId());
        permissionOrchestrator.requireCompanyEmployee(client.getCompany().getId());
        validateAddress(client.getAddress());

        return clientService.save(client);
    }

    public Client update(Client client, Long companyId) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            client.setCompany(permissionOrchestrator.getCurrentCompany());
        else {
            client.setCompany(companyService.findById(companyId));
        }

        permissionOrchestrator.requireCompanyEmployee(client.getCompany().getId());
        Client updated = clientService.findById(client.getId());

        if (!client.getEmail().equals(updated.getEmail())) {
            clientService.existsByEmailAndCompanyId(client.getEmail(), client.getCompany().getId());
        }

        updated.setName(client.getName());
        updated.setLastname(client.getLastname());
        updated.setPhone(client.getPhone());
        updated.setEmail(client.getEmail());

        if (client.getAddress() != null) {
            validateAddress(client.getAddress());
            updated.setAddress(client.getAddress());
        }

        return clientService.update(updated);

    }

    public Client delete(Long id) {
        Client existing = clientService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getCompany().getId());
        return clientService.delete(existing);
    }

    private void validateAddress(Address address) {
        if (address == null)
            return;
        if (!addressService.existsByStreetAndCityAndNumber(address.getStreet(), address.getCity(),
                address.getNumber())) {
            addressService.save(address);
        }
    }
}

package com.backend.api.invoice_manager.services.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.dtos.TopClientDTO;
import com.backend.api.invoice_manager.entities.Client;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.ClientRepository;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;

    @Transactional(readOnly = true)
    @Override
    public void existsByEmailAndCompanyId(String email, Long companyId) {
        if (repository.existsByEmailAndCompanyId(email, companyId))
            throw new AlreadyExistsException(email);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        List<Client> clients = (List<Client>) repository.findAll();
        if (clients.isEmpty())
            throw new NotFoundException("No clients found in the database.");

        return clients;
    }

    public List<Client> findByCompanyId(Long companyId) {
        List<Client> clients = (List<Client>) repository.findByCompanyId(companyId);
        if (clients.isEmpty())
            throw new NotFoundException("No clients found with the provided criteria.");

        return clients;
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        Client client = repository.findById(id).orElseThrow(
                () -> new NotFoundException("Client", "id", id));

        return client;
    }

    @Transactional(readOnly = true)
    @Override
    public Client findByIdAndCompanyId(Long id, Long companyId) {
        Client client = repository.findByIdAndCompanyId(id, companyId).orElseThrow(
                () -> new NotFoundException("Client", "id", id));

        return client;
    }

    @Transactional(readOnly = true)
    @Override
    public Client findByEmailAndCompanyId(String email, Long companyId) {
        return repository.findByEmailAndCompanyId(email, companyId).orElseThrow(
                () -> new NotFoundException("Client", "email", email));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> searchClients(String term) {
        List<Client> clients = (List<Client>) repository.searchClients(term);
        if (clients.isEmpty())
            throw new NotFoundException("No clients found with the provided criteria.");
        return clients;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> searchClientsByCompany(String term, Long companyId) {
        List<Client> clients = (List<Client>) repository.searchClientsByCompany(term, companyId);
        if (clients.isEmpty())
            throw new NotFoundException("No clients found with the provided criteria.");
        return clients;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopClientDTO> getTopClients() {
        return repository.findTopClients();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopClientDTO> getTopClientsByCompanyId(Long companyId) {
        return repository.findTopClientsByCompanyId(companyId);
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Client client) {
        return repository.save(client);
    }

    @Override
    public Client delete(Client client) {
        repository.delete(client);
        return client;
    }
}
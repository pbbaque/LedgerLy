package com.backend.api.invoice_manager.services.client;

import java.util.List;

import com.backend.api.invoice_manager.dtos.TopClientDTO;
import com.backend.api.invoice_manager.entities.Client;

public interface ClientService {

    void existsByEmailAndCompanyId(String email, Long companyId);

    List<Client> findAll();

    List<Client> findByCompanyId(Long companyId);

    Client findById(Long id);

    Client findByIdAndCompanyId(Long id, Long companyId);

    Client findByEmailAndCompanyId(String email, Long companyId);
    
    List<Client> searchClients(String term);
    
    List<Client> searchClientsByCompany(String term, Long companyId);

    List<TopClientDTO> getTopClients();

    List<TopClientDTO> getTopClientsByCompanyId(Long companyId);

    Client save(Client client);

    Client update(Client client);

    Client delete(Client client);

}

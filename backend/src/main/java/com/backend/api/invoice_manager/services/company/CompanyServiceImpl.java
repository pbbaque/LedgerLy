package com.backend.api.invoice_manager.services.company;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.CompanyRepository;

@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository repository;

    @Transactional(readOnly = true)
    @Override
    public void existsByFiscalNumber(String fiscalNumber) {
        if (repository.existsByFiscalNumber(fiscalNumber)) {
            throw new AlreadyExistsException("Company", "fiscal number", fiscalNumber);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void existsByEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new AlreadyExistsException("Company", "email", email);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Company> findAll() {
        List<Company> companies = (List<Company>) repository.findAll();
        if (companies.isEmpty()) {
            throw new NotFoundException("No companies found in the database.");
        }
        return companies;
    }

    @Transactional(readOnly = true)
    @Override
    public Company findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company", "id", id));
    }

    @Transactional(readOnly = true)
    @Override
    public Company findByFiscalNumber(String fiscalNumber) {
        return repository.findByFiscalNumber(fiscalNumber)
                .orElseThrow(() -> new NotFoundException("Company", "fiscal number", fiscalNumber));
    }

    @Transactional(readOnly = true)
    @Override
    public Company findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Company", "email", email));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Company> searchCompanies(String term) {
        List<Company> companies = (List<Company>) repository.searchCompanies(term);
        if (companies.isEmpty())
            throw new NotFoundException("No companies found with the provided criteria.");
        return companies;
    }

    @Override
    public Company save(Company company) {
        return repository.save(company);
    }

    @Override
    public Company update(Company company) {
        return repository.save(company);
    }

    @Override
    public Company delete(Company company) {
        repository.delete(company);
        return company;
    }
}

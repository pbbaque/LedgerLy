package com.backend.api.invoice_manager.services.company;

import java.util.List;

import com.backend.api.invoice_manager.entities.Company;

public interface CompanyService {

    void existsByFiscalNumber(String fiscalNumber);

    void existsByEmail(String email);

    List<Company> findAll();

    Company findById(Long id);

    Company findByFiscalNumber(String fiscalNumber);

    Company findByEmail(String email);

    List<Company> searchCompanies(String term);

    Company save(Company company);

    Company update(Company company);

    Company delete(Company company);

}

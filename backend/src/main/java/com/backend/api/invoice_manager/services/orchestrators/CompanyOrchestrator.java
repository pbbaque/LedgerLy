package com.backend.api.invoice_manager.services.orchestrators;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Address;
import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.RoleRepository;
import com.backend.api.invoice_manager.services.address.AddressService;
import com.backend.api.invoice_manager.services.company.CompanyService;
import com.backend.api.invoice_manager.services.email.EmailService;
import com.backend.api.invoice_manager.services.employee.EmployeeService;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;
import com.backend.api.invoice_manager.services.user.UserService;
import com.backend.api.invoice_manager.utils.PasswordGenerator;

@Component
public class CompanyOrchestrator {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PermissionOrchestrator permissionOrchestrator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AddressService addressService;

    private PasswordGenerator passwordGenerator = new PasswordGenerator();

    public List<Company> findAll() {
        permissionOrchestrator.requireSuperAdminOrAdmin();
        return companyService.findAll();
    }

    public Company findById(Long id) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(id);
        return companyService.findById(id);
    }

    public Company getCurrentCompany() {
        return permissionOrchestrator.getCurrentCompany();
    }

    public Company findByFiscalNumber(String fiscalNumber) {
        Company company = companyService.findByFiscalNumber(fiscalNumber);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(company.getId());
        return company;
    }

    public Company findByEmail(String email) {
        Company company = companyService.findByEmail(email);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(company.getId());
        return company;
    }

    public List<Company> searchCompanies(String term) {
        permissionOrchestrator.requireSuperAdminOrAdmin();
        return companyService.searchCompanies(term);
    }

    public Company create(Company company) {
        companyService.existsByFiscalNumber(company.getFiscalNumber());
        companyService.existsByEmail(company.getEmail());

        if (company.getAddress() == null)
            throw new NotFoundException("Address can not be null.");

        validateAddress(company.getAddress());

        Company created = companyService.save(company);

        String plainPassword = passwordGenerator.generateRandomPassword();
        User adminUser = createAdminUserForCompany(created, plainPassword);
        Employee adminEmployee = createAdminEmployeeForCompany(created, adminUser);

        sendAdminCredentials(adminUser, adminEmployee, created, plainPassword);

        return created;
    }

    public Company update(Company company) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(company.getId());

        Company updated = companyService.findById(company.getId());

        if (!company.getFiscalNumber().equals(updated.getFiscalNumber())) {
            companyService.existsByFiscalNumber(company.getFiscalNumber());
        }
        if (!company.getEmail().equals(updated.getEmail())) {
            companyService.existsByEmail(company.getEmail());
        }

        updated.setName(company.getName());
        updated.setFiscalNumber(company.getFiscalNumber());
        updated.setPhone(company.getPhone());
        updated.setEmail(company.getEmail());

        if (company.getAddress() != null) {
            validateAddress(company.getAddress());
            updated.setAddress(company.getAddress());
        }

        return companyService.update(updated);
    }

    public Company delete(Long id) {
        permissionOrchestrator.requireSuperAdminOrAdmin();
        Company deleted = companyService.findById(id);
        return companyService.delete(deleted);
    }

    private User createAdminUserForCompany(Company company, String plainPassword) {
        User admin = new User();
        admin.setUsername(company.getName() + "super_admin_company");
        admin.setPassword(plainPassword);
        admin.setEmail(company.getEmail());
        admin.setEnabled(true);
        admin.setRoles(List.of(roleRepository.findByName("ROLE_COMPANY_SUPER_ADMIN").orElseThrow(
                () -> new NotFoundException("Role", "name", "ROLE_COMPANY_SUPER_ADMIN"))));
        return userService.save(admin);
    }

    private Employee createAdminEmployeeForCompany(Company company, User adminUser) {
        Employee admin = new Employee();
        admin.setUser(adminUser);
        admin.setCompany(company);
        admin.setName(company.getName() + " Super Admin");
        admin.setLastname("Main Admin");
        admin.setPhone(company.getPhone());
        admin.setEmail(company.getEmail());
        admin.setPosition("Administrator");
        admin.setHireDate(new Date());
        admin.setSalary(0);

        return employeeService.save(admin);
    }

    private void sendAdminCredentials(User adminUser, Employee adminEmployee, Company company, String plainPassword) {
        emailService.sendUserCredentials(adminUser.getEmail(), adminEmployee.getName(), plainPassword,
                company.getName());
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

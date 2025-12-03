package com.backend.api.invoice_manager.services.employee;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.EmployeeRepository;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    @Override
    @Transactional(readOnly = true)
    public void existsByEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new AlreadyExistsException("Employee already exists with email: " + email);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        List<Employee> employees = (List<Employee>) repository.findAll();
        if (employees.isEmpty()) {
            throw new NotFoundException("No employees found in the database.");
        }
        return employees;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByIdAndCompanyId(Long id, Long companyId) {
        return repository.findByIdAndCompanyId(id, companyId)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id + " and company id: " + companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Employee not found with user id: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByUserIdAndCompanyId(Long userId, Long companyId) {
        return repository.findByUserIdAndCompanyId(userId, companyId)
                .orElseThrow(() -> new NotFoundException("Employee not found with user id: " + userId + " and company id: " + companyId));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Employee not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByCompanyId(Long companyId) {
        List<Employee> employees = repository.findByCompanyId(companyId);
        if (employees.isEmpty()) {
            throw new NotFoundException("No employees found for the company with id: " + companyId);
        }
        return employees;
    }

    @Transactional(readOnly = true) 
    @Override
    public List<Employee> searchEmployees(String term) {
        List<Employee> employees = (List<Employee>) repository.searchEmployees(term);
        if (employees.isEmpty())
            throw new NotFoundException("No users found with the provided criteria.");
        return employees;
    }
    
    @Transactional(readOnly = true) 
    @Override
    public List<Employee> searchEmployeesByCompany(String term, Long companyId) {
        List<Employee> employees = (List<Employee>) repository.searchEmployeesByCompany(term, companyId);
        if (employees.isEmpty())
            throw new NotFoundException("No users found with the provided criteria.");
        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        existsByEmail(employee.getEmail());
        employee.setHireDate(new Date());
        return repository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        Employee existing = findById(employee.getId());
        existing.setName(employee.getName());
        existing.setLastname(employee.getLastname());
        existing.setEmail(employee.getEmail());
        existing.setPhone(employee.getPhone());
        existing.setPosition(employee.getPosition());
        existing.setSalary(employee.getSalary());
        existing.setHireDate(employee.getHireDate());
        return repository.save(existing);
    }

    @Override
    public Employee delete(Long id) {
        Employee employee = findById(id);
        repository.delete(employee);
        return employee;
    }
}

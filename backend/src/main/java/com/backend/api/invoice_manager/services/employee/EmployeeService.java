package com.backend.api.invoice_manager.services.employee;

import java.util.List;
import com.backend.api.invoice_manager.entities.Employee;

public interface EmployeeService {
    
    void existsByEmail(String email);
    
    List<Employee> findAll();
    
    Employee findById(Long id);

    Employee findByIdAndCompanyId(Long id, Long companyId);
    
    Employee findByUserId(Long userId);
   
    Employee findByUserIdAndCompanyId(Long userId, Long companyId);

    Employee findByEmail(String email);

    List<Employee> findByCompanyId(Long companyId);

    List<Employee> searchEmployees(String term);
    
    List<Employee> searchEmployeesByCompany(String term, Long companyId);

    Employee save(Employee employee);

    Employee update(Employee employee);

    Employee delete(Long id);

}

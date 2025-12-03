package com.backend.api.invoice_manager.services.orchestrators;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.services.company.CompanyService;
import com.backend.api.invoice_manager.services.employee.EmployeeService;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;
import com.backend.api.invoice_manager.services.user.UserService;

@Component
public class EmployeeOrchestrator {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PermissionOrchestrator permissionOrchestrator;

    public List<Employee> findAll() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return employeeService.findByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return employeeService.findAll();
    }

    public List<Employee> findByCompanyId(Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return employeeService.findByCompanyId(companyId);
    }

    public Employee findById(Long id) {
        Employee employee = employeeService.findById(id);
        if (!permissionOrchestrator.isSuperAdminOrAdmin() && permissionOrchestrator.isCompanyRole())
            return employeeService.findByIdAndCompanyId(id,permissionOrchestrator.getCurrentCompany().getId());
        return employee;
    }

    public Employee findByUserId(Long userId) {
        Employee employee = employeeService.findByUserId(userId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());
        return employee;
    }

    public Employee findByEmail(String email) {
        Employee employee = employeeService.findByEmail(email);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());
        return employee;
    }

    public List<Employee> searchEmployees(String term) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin() && permissionOrchestrator.isCompanyRole())
            return employeeService.searchEmployeesByCompany(term, permissionOrchestrator.getCurrentCompany().getId());
        return employeeService.searchEmployees(term);
    }

    public List<Employee> searchEmployeesByCompany(String term, Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return employeeService.searchEmployeesByCompany(term, companyId);
    }

    public Employee create(Employee employee) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());
        Employee saved = employeeService.save(employee);
        assignUser(employee.getUser().getId(), saved.getId());
        return saved;
    }

    public Employee createEmployeeWithUser(Employee employee, User user) {
        Long targetCompanyId = employee.getCompany() != null ? employee.getCompany().getId() : null;

        permissionOrchestrator.validateUserCreationRoles(user, employee.getId());
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(targetCompanyId);

        User savedUser = userService.save(user);
        employee.setUser(savedUser);

        return employeeService.save(employee);
    }

    public User createUserForExistingEmployee(Long employeeId, User user) {
        permissionOrchestrator.validateUserCreationRoles(user, employeeId);

        User created = userService.save(user);
        assignUser(created.getId(), employeeId);

        return created;
    }

    public Employee update(Employee employee) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());
        return employeeService.update(employee);
    }

    public Employee delete(Long id) {
        Employee employee = employeeService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());
        return employeeService.delete(id);
    }

    public Employee assignUser(Long userId, Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());

        if (employee.getUser() != null) {
            throw new AlreadyExistsException("Employee already has a user assigned.");
        }
        employee.setUser(userService.findById(userId));
        return employeeService.save(employee);
    }

    public Employee assignCompany(Long companyId, Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        if (employee.getCompany() != null) {
            throw new AlreadyExistsException("Employee already has a company assigned.");
        }

        employee.setCompany(companyService.findById(companyId));
        return employeeService.save(employee);
    }

    public Employee removeUser(Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());

        employee.setUser(null);
        return employeeService.save(employee);
    }

    public Employee removeCompany(Long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(employee.getCompany().getId());

        employee.setCompany(null);
        return employeeService.save(employee);
    }
}

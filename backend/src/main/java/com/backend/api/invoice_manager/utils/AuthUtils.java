package com.backend.api.invoice_manager.utils;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.UserRepository;
import com.backend.api.invoice_manager.services.employee.EmployeeService;

@Component
public class AuthUtils {

    private final UserRepository userRepository;

    private final ApplicationContext context;

    public AuthUtils(UserRepository userRepository, ApplicationContext context) {
        this.userRepository = userRepository;
        this.context = context;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in the security context.");
        }

        return authentication.getName();
    }

    public User getCurrentUser() {
        String email = getCurrentUsername();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User", "email", email));
    }

    public Employee getCurrentEmployee() {
        return context.getBean(EmployeeService.class).findByUserId(getCurrentUser().getId());
    }

    public Company getCurrentCompany() {
        return getCurrentEmployee().getCompany();
    }

    public boolean isSuperAdminOrAdmin() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_SUPER_ADMIN") || role.getName().equals("ROLE_ADMIN"));
    }

    public boolean isSuperAdmin() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_SUPER_ADMIN"));
    }

    public boolean isAdmin() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_ADMIN"));
    }

    public boolean isCompanyRole() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_SUPER_ADMIN_COMPANY") || 
            role.getName().equals("ROLE_ADMIN_COMPANY"));
    }

    public boolean isSuperAdminCompanyRole() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_SUPER_ADMIN_COMPANY"));
    }

    public boolean isAdminCompanyRole() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_ADMIN_COMPANY"));
    }

    public boolean isCompanyEmployee(Long companyId) {
        return getCurrentCompany().getId().equals(companyId);
    }

    public boolean isUserRole() {
        return getCurrentUser().getRoles().stream().anyMatch( role -> 
            role.getName().equals("ROLE_USER"));
    }

    public boolean isOnlyUserRole() {
        return getCurrentUser().getRoles().size() == 1 && isUserRole();
    }

    public boolean hasManagementRole() {
        return isSuperAdminOrAdmin() || isCompanyRole();
    }

    public List<String> getCurrentUserRoleNames() {
        return getCurrentUser().getRoles().stream()
            .map(role -> role.getName())
            .collect(Collectors.toList());
    }

    public boolean canManageEmployee(Long employeeId) {
        if(!isCompanyRole()) 
            return false;
        try {
            Employee employee = context.getBean(EmployeeService.class).findById(employeeId);

            return employee.getCompany().getId().equals(getCurrentCompany().getId());
        } catch(Exception e) {
            return false;
        }
    }

    public boolean canManageCompany(Long companyId) {
        if(isSuperAdminOrAdmin()) 
            return true;
        
        if(isCompanyRole())
            return getCurrentCompany().getId().equals(companyId);

        return false;
    }
}

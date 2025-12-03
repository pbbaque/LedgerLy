package com.backend.api.invoice_manager.services.orchestrators.permission;

import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Company;
import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.UnauthorizedActionException;
import com.backend.api.invoice_manager.utils.AuthUtils;
import com.backend.api.invoice_manager.validators.UserPermissionValidator;

@Component
public class PermissionOrchestrator {

    private final AuthUtils authUtils;
    private final UserPermissionValidator userPermissionValidator;

    public PermissionOrchestrator(AuthUtils authUtils, UserPermissionValidator userPermissionValidator) {
        this.authUtils = authUtils;
        this.userPermissionValidator  = userPermissionValidator;
    }



  public User getCurrentUser() {
        return authUtils.getCurrentUser();
    }

    public Employee getCurrentEmployee() {
        return authUtils.getCurrentEmployee();
    }

    public Company getCurrentCompany() {
        return authUtils.getCurrentCompany();
    }

    public boolean isSuperAdminOrAdmin() {
        return authUtils.isSuperAdminOrAdmin();
    }

    public boolean isSuperAdmin() {
        return authUtils.isSuperAdmin();
    }

    public boolean isAdmin() {
        return authUtils.isAdmin();
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

    public void requireSuperAdminOrAdmin() {
        if(!isSuperAdminOrAdmin())
            throw new UnauthorizedActionException("User does not have administrator privileges.");

    }

    public void requireCompanyRole() {
        if(!isCompanyRole())
            throw new UnauthorizedActionException("User does not have company-level role.");
    }

    public void requireNotOnlyUserRole() {
        if(authUtils.isOnlyUserRole())
            throw new UnauthorizedActionException("Users with only USER role cannot perform this action.");
    }
    
    public void requireCompanyEmployee(Long companyId) {
        if(!authUtils.isSuperAdminOrAdmin() && !authUtils.isCompanyEmployee(companyId))
            throw new UnauthorizedActionException("Cannot access resources from different company.");
    }

    public void requireCanManageCompany(Long companyId) {
        if(!authUtils.canManageCompany(companyId))
            throw new UnauthorizedActionException("User cannot manage this company.");
    }

    public void requireCanManageEmployee(Long employeeId) {
        if(!authUtils.canManageEmployee(employeeId))
            throw new UnauthorizedActionException("User cannot manage this employee.");
    }

    public void requireManagementRole() {
        if(!authUtils.hasManagementRole())
            throw new UnauthorizedActionException("User does not have management permissions.");
    }

    public void validateUserCreationRoles(User user, Long employeeId){
        userPermissionValidator.validateUserCreation(user, employeeId);
    }

    public void validateCompanyUserCreation(User user, Long companyId) {
        userPermissionValidator.validateCompanyUserCreation(user, companyId);
    }

    public void validateRoleConsistency(List<Role> roles) {
        userPermissionValidator.validateRoleConsistency(roles);
    }

    public List<String> getAllowedRolesToCreate() {
        return userPermissionValidator.getAllowedRolesToCreate();
    }

    public void requireCompanyOwnershipOrAdmin(Long companyId) {
        requireNotOnlyUserRole();
        if (!authUtils.isSuperAdminOrAdmin()) {
            requireCompanyEmployee(companyId);
        }
    }
}

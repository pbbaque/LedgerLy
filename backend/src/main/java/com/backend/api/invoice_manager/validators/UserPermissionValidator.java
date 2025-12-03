package com.backend.api.invoice_manager.validators;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.BusinessException;
import com.backend.api.invoice_manager.services.employee.EmployeeService;
import com.backend.api.invoice_manager.utils.AuthUtils;

@Component
public class UserPermissionValidator {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private ApplicationContext context;

    public void validateUserCreation(User user, Long employeeId) {
        if(authUtils.isOnlyUserRole())
            throw new AccessDeniedException("Users with only USER role cannot create other users");
        
        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        if(roleNames.isEmpty())
            roleNames.add("ROLE_USER");
        
        for(String roleName : roleNames) {
            validateRoleCreationPermission(roleName);
        }

        if(employeeId != null)
            validateEmployeeCompanyRestrictions(employeeId, roleNames);
    }

    public void validateCompanyUserCreation(User user, Long companyId) {
        if(authUtils.isOnlyUserRole())
            throw new AccessDeniedException("Users with only USER role cannot create other users");
        
        if(!authUtils.canManageCompany(companyId))
            throw new AccessDeniedException("User cannot create users for this company");

        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        if(roleNames.isEmpty())
            roleNames.add("ROLE_USER");
        
        for(String roleName : roleNames){
            validateRoleCreationPermission(roleName);

            if(isCompanyRole(roleNames) && authUtils.isCompanyRole())
                if(!authUtils.canManageCompany(companyId)) 
                    throw new AccessDeniedException(String.format("Cannot assign company role %s to user outside your company", roleName));

        }
    }

    public void validateEmployeeCreation(Long companyId){
        if(authUtils.isOnlyUserRole())
            throw new AccessDeniedException("Users with only USER role cannot create other users");

        if(!authUtils.hasManagementRole())
            throw new AccessDeniedException("User does not have permission to create employees");

        if(authUtils.isCompanyRole() && companyId != null)
            if(!authUtils.canManageCompany(companyId))
                throw new AccessDeniedException("Cannot create employee for different company");
    }

    private void validateEmployeeCompanyRestrictions(Long employeeId, List<String> roleNames){
        if(!authUtils.isCompanyRole())
            return;

        try {
            Employee employee = context.getBean(EmployeeService.class).findById(employeeId);

            if(!authUtils.canManageEmployee(employeeId))
                throw new AccessDeniedException("Cannot create user for employee from different company");

            validateRoleHierarchy(roleNames, employee.getCompany().getId());
        } catch(Exception e) {
            throw new AccessDeniedException("Cannot validate employee company restrictions: " + e.getMessage());
        }
    }

    private void validateRoleHierarchy(List<String> roleNames, Long companyId) {
        if(authUtils.isAdminCompanyRole() && !authUtils.isSuperAdminCompanyRole())
            if(roleNames.contains("ROLE_SUPER_ADMIN_COMPANY"))
                throw new AccessDeniedException("ADMIN_COMPANY cannot create SUPER_ADMIN_COMPANY users");
    }

    private void validateRoleCreationPermission(String roleName) {
        List<String> allowedRoles = getAllowedRolesToCreate();
        if(!allowedRoles.contains(roleName))
            throw new AccessDeniedException(String.format("User cannot create users with role: %s", roleName));

        return;
    }

    public List<String> getAllowedRolesToCreate() {
        if (authUtils.isSuperAdmin()) {
            return Arrays.asList("ROLE_USER", "ROLE_ADMIN_COMPANY", "ROLE_SUPER_ADMIN_COMPANY", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        }

        if (authUtils.isAdmin()) {
            return Arrays.asList("ROLE_ADMIN_COMPANY", "ROLE_SUPER_ADMIN_COMPANY", "ROLE_USER");
        }

        if (authUtils.isSuperAdminCompanyRole()) {
            return Arrays.asList("ROLE_USER", "ROLE_ADMIN_COMPANY");
        }

        if (authUtils.isAdminCompanyRole()) {
            return Arrays.asList("ROLE_USER");
        }

        return Arrays.asList();
    }

    private boolean isCompanyRole(List<String> roleNames) {
        List<String> companyRoles = Arrays.asList("ROLE_ADMIN_COMPANY", "ROLE_SUPER_ADMIN_COMPANY");
        return roleNames.stream().anyMatch(companyRoles::contains);
    }

    public void validateRoleConsistency(List<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        boolean hasGlobalRole = roleNames.stream()
                .anyMatch(name -> name.equals("ROLE_ADMIN") || name.equals("ROLE_SUPER_ADMIN"));
        
        boolean hasCompanyRole = roleNames.stream()
                .anyMatch(name -> name.equals("ROLE_ADMIN_COMPANY") || name.equals("ROLE_SUPER_ADMIN_COMPANY"));

        if (hasGlobalRole && hasCompanyRole) {
            throw new BusinessException("Cannot assign both global and company-specific roles to the same user");
        }

        boolean hasSuperAdminCompany = roleNames.contains("ROLE_SUPER_ADMIN_COMPANY");
        boolean hasAdminCompany = roleNames.contains("ROLE_ADMIN_COMPANY");
        
        if (hasSuperAdminCompany && hasAdminCompany) {
            throw new BusinessException("Cannot assign both SUPER_ADMIN_COMPANY and ADMIN_COMPANY to the same user");
        }
    }

    public void validateUserAssignmentToEmployee(Long userId, Long employeeId) {
        if (!authUtils.isSuperAdminOrAdmin()) {
            throw new AccessDeniedException("User does not have permission to assign users to employees");
        }

        if (authUtils.isCompanyRole()) {
            if (!authUtils.canManageEmployee(employeeId)) {
                throw new AccessDeniedException("Cannot assign user to employee from different company");
            }
        }
    }
}

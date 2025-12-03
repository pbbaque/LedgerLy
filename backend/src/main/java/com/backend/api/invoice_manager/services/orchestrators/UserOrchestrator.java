package com.backend.api.invoice_manager.services.orchestrators;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.UnauthorizedActionException;
import com.backend.api.invoice_manager.repositories.RoleRepository;
import com.backend.api.invoice_manager.services.email.EmailService;
import com.backend.api.invoice_manager.services.employee.EmployeeService;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;
import com.backend.api.invoice_manager.services.user.UserService;
import com.backend.api.invoice_manager.utils.PasswordGenerator;

@Component
public class UserOrchestrator {

    @Autowired
    private UserService userService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionOrchestrator permissionOrchestrator;
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private EmailService emailService;

    private List<Role> assignDefaultRoles(User user, boolean isPublicRegistration) {
        List<Role> assignedRoles = new ArrayList<>();
        Role userRole = userService.getRoleByName("ROLE_USER");
        assignedRoles.add(userRole);

        if (isPublicRegistration) {
            return assignedRoles;
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            permissionOrchestrator.validateRoleConsistency(user.getRoles());
            permissionOrchestrator.validateUserCreationRoles(user,
                    user.getEmployee() != null ? user.getEmployee().getId() : null);
            return user.getRoles();
        }

        if (user.isAdmin()) {
            if (permissionOrchestrator.isSuperAdmin()) {
                Role adminRole = userService.getRoleByName("ROLE_ADMIN");
                assignedRoles.add(adminRole);
            } else {
                user.setAdmin(false);
            }
        }

        return assignedRoles;
    }

    public List<User> findAll() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return findByEmployeeCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return userService.findAll();
    }

    public List<User> findByEmployeeCompanyId(Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return userService.findByEmployeeCompanyId(companyId);
    }

    public User getCurrentUser() {
        return permissionOrchestrator.getCurrentUser();
    }

    public User findById(Long id) {
        User user = userService.findById(id);
        if (user.getEmployee() != null)
            permissionOrchestrator.requireCompanyOwnershipOrAdmin(user.getEmployee().getCompany().getId());
        else
            permissionOrchestrator.requireSuperAdminOrAdmin();
        return user;
    }

    public User findByEmail(String email) {
        User user = userService.findByEmail(email);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(user.getEmployee().getCompany().getId());
        return user;
    }

    public List<User> searchUsers(String term) {
        if (!permissionOrchestrator.isSuperAdminOrAdmin() && permissionOrchestrator.isCompanyRole())
            return userService.searchUsersByCompany(term, permissionOrchestrator.getCurrentCompany().getId());
        return userService.searchUsers(term);
    }

    public List<User> searchUsersByCompany(String term, Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return userService.searchUsersByCompany(term, companyId);
    }

    public User create(User user) {
        user.setRoles(assignDefaultRoles(user, false));
        permissionOrchestrator.requireSuperAdminOrAdmin();
        permissionOrchestrator.validateUserCreationRoles(user,
                user.getEmployee() != null ? user.getEmployee().getId() : null);
        return userService.save(user);
    }

    public User createUserForCompany(User user, Long companyId) {
        permissionOrchestrator.validateCompanyUserCreation(user, companyId);
        return userService.save(user);
    }

    public void proccessForgotPassword(String email) {
        User user = userService.findByEmail(email);
        String plainPassword = passwordGenerator.generateRandomPassword();
        userService.changePassword(user.getEmail(), plainPassword);
        emailService.sendPasswordResetLink(user.getEmail(), plainPassword, user.getUsername());
    }

    public User update(User user) {
        permissionOrchestrator.validateRoleConsistency(user.getRoles());
        User existing = userService.findById(user.getId());
        Long employeeId = existing.getEmployee() != null ? existing.getEmployee().getId() : null;
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getEmployee().getCompany().getId());
        permissionOrchestrator.validateUserCreationRoles(user, employeeId);
        return userService.update(user);
    }

    public User delete(Long id) {
        User user = userService.findById(id);

        if (permissionOrchestrator.getCurrentUser().getId().equals(user.getId())) {
            throw new UnauthorizedActionException("User cannot delete themselves.");
        }

        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            permissionOrchestrator.requireCompanyOwnershipOrAdmin(user.getEmployee().getCompany().getId());

        List<String> allowedRoles = permissionOrchestrator.getAllowedRolesToCreate();
        List<String> userRoles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        boolean canDeleteUser = userRoles.stream().allMatch(allowedRoles::contains);
        if (!canDeleteUser) {
            throw new UnauthorizedActionException("User does not have permission to delete users with these roles");
        }

        Employee employee = employeeService.findByUserId(id);
        employee.setUser(null);
        employeeService.update(employee);

        return userService.delete(id);
    }

    public List<Role> findAllRoles() {
        return (List<Role>) roleRepository.findAll();
    }
}

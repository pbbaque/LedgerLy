package com.backend.api.invoice_manager.services.orchestrators.permission;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.UnauthorizedActionException;
import com.backend.api.invoice_manager.utils.AuthUtils;
import com.backend.api.invoice_manager.validators.UserPermissionValidator;

@ExtendWith(MockitoExtension.class)
class PermissionOrchestratorTest {

    @Mock
    private AuthUtils authUtils;

    @Mock
    private UserPermissionValidator userPermissionValidator;

    private PermissionOrchestrator permissionOrchestrator;

    @BeforeEach
    void setUp() {
        permissionOrchestrator = new PermissionOrchestrator(authUtils, userPermissionValidator);
    }

    @Test
    void allowsAdministratorActionsForSuperAdminOrAdmin() {
        when(authUtils.isSuperAdminOrAdmin()).thenReturn(true);

        assertDoesNotThrow(() -> permissionOrchestrator.requireSuperAdminOrAdmin());
    }

    @Test
    void rejectsAdministratorActionsForNonAdminUsers() {
        when(authUtils.isSuperAdminOrAdmin()).thenReturn(false);

        assertThrows(UnauthorizedActionException.class, () -> permissionOrchestrator.requireSuperAdminOrAdmin());
    }

    @Test
    void allowsCompanyOwnershipForGlobalAdministrators() {
        when(authUtils.isOnlyUserRole()).thenReturn(false);
        when(authUtils.isSuperAdminOrAdmin()).thenReturn(true);

        assertDoesNotThrow(() -> permissionOrchestrator.requireCompanyOwnershipOrAdmin(1L));
    }

    @Test
    void rejectsCompanyOwnershipForOnlyUserRole() {
        when(authUtils.isOnlyUserRole()).thenReturn(true);

        assertThrows(UnauthorizedActionException.class, () -> permissionOrchestrator.requireCompanyOwnershipOrAdmin(1L));
    }

    @Test
    void validatesCompanyEmployeeWhenUserIsNotGlobalAdmin() {
        when(authUtils.isOnlyUserRole()).thenReturn(false);
        when(authUtils.isSuperAdminOrAdmin()).thenReturn(false);
        when(authUtils.isCompanyEmployee(1L)).thenReturn(true);

        assertDoesNotThrow(() -> permissionOrchestrator.requireCompanyOwnershipOrAdmin(1L));
        verify(authUtils).isCompanyEmployee(1L);
    }

    @Test
    void detectsCompanyManagementRolesFromCurrentUser() {
        User user = new User();
        user.setRoles(List.of(new Role("ROLE_ADMIN_COMPANY")));
        when(authUtils.getCurrentUser()).thenReturn(user);

        assertDoesNotThrow(() -> permissionOrchestrator.requireCompanyRole());
    }
}

package com.backend.api.invoice_manager.services.user;

import java.util.List;

import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;

public interface UserService {

    void existsByEmail(String email);

    List<User> findAll(); 

    List<User> findByEmployeeCompanyId(Long companyId); 

    User findById(Long id);

    User findByEmail(String email);

    User save(User user);

    void changePassword(String email, String newPassword);

    List<User> searchUsers(String term);
    
    List<User> searchUsersByCompany(String term, Long companyId);

    User update(User user);

    User delete(Long id);

    Role getRoleByName(String roleName);
}

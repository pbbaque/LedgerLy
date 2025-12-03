package com.backend.api.invoice_manager.services.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.entities.Role;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.RoleRepository;
import com.backend.api.invoice_manager.repositories.UserRepository;

import jakarta.validation.ValidationException;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public void existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("User already exists with email: " + email);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundException("No users found in the database.");
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByEmployeeCompanyId(Long companyId) {
        List<User> users = (List<User>) userRepository.findByEmployeeCompanyId(companyId);
        if (users.isEmpty()) {
            throw new NotFoundException("No users found in the database.");
        }
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User", "email", email));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> searchUsers(String term) {
        List<User> users = (List<User>) userRepository.searchUsers(term);
        if (users.isEmpty())
            throw new NotFoundException("No users found with the provided criteria.");
        return users;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> searchUsersByCompany(String term, Long companyId) {
        List<User> users = (List<User>) userRepository.searchUsersByCompany(term, companyId);
        if (users.isEmpty())
            throw new NotFoundException("No users found with the provided criteria.");
        return users;
    }

    @Override
    public User save(User user) {
        existsByEmail(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String newPassword) {
        User user = findByEmail(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User update(User user) {
        User existing = findById(user.getId());

        if (!existing.getEmail().equals(user.getEmail())) {
            existsByEmail(user.getEmail());
        }

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()){

            if (user.getPassword().length() < 8) 
               throw new ValidationException("Password must be at least 8 characters long");
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existing.setRoles(user.getRoles());
        existing.setAdmin(user.isAdmin());

        return userRepository.save(existing);
    }

    @Override
    public User delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
        return user;
    }

    @Transactional(readOnly = true)
    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role", "name", roleName));
    }
}

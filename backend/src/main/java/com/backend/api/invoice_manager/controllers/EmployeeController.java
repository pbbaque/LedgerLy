package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.invoice_manager.dtos.EmployeeWithUserDTO;
import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.User;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.EmployeeOrchestrator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeOrchestrator orchestrator;

    @GetMapping
    public ResponseEntity<?> findAll() {
        ApiResponse<List<Employee>> response = new ApiResponse<>("Employees retrieved successfully.",
                orchestrator.findAll(), HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Employee employee = orchestrator.findById(id);
        ApiResponse<Employee> response = new ApiResponse<>("Employee with id: " + id + " found successfully.", employee,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/by-email")
    public ResponseEntity<?> findByEmail(@RequestParam String email) {
        Employee employee = orchestrator.findByEmail(email);
        ApiResponse<Employee> response = new ApiResponse<>("Employee with email: " + email + " found successfully.",
                employee, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> findByCompanyId(@PathVariable Long companyId) {
        List<Employee> employees = orchestrator.findByCompanyId(companyId);
        ApiResponse<List<Employee>> response = new ApiResponse<>("Employees found for company with id: " + companyId,
                employees, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search/all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> searchEmployess(@RequestParam(required = false) String term) {
        List<Employee> employees = orchestrator.searchEmployees(term);
        return ResponseEntity.ok(new ApiResponse<>("Search results.", employees, HttpStatus.OK.value(), true));
    }

    @GetMapping("/search/all/company/{companyId}/search")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','SUPER_ADMIN_COMPANY','ADMIN_COMPANY')")
    public ResponseEntity<?> searchEmployeesByCompany(@PathVariable Long companyId,
            @RequestParam(required = false) String term) {
        List<Employee> employees = orchestrator.searchEmployeesByCompany(term, companyId);
        return ResponseEntity
                .ok(new ApiResponse<>("Search results for company.", employees, HttpStatus.OK.value(), true));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Employee employee) {
        Employee createdEmployee = orchestrator.create(employee);
        ApiResponse<Employee> response = new ApiResponse<>("Employee created successfully.", createdEmployee,
                HttpStatus.CREATED.value(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/with-user")
    public ResponseEntity<?> createEmployeeWithUser(
            @Valid @RequestBody EmployeeWithUserDTO dto) {

        Employee createdEmployee = orchestrator.createEmployeeWithUser(dto.getEmployee(), dto.getUser());
        ApiResponse<Employee> response = new ApiResponse<>("Employee and User created successfully.", createdEmployee,
                HttpStatus.CREATED.value(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/for-employee/{employeeId}")
    public ResponseEntity<?> createUserForExistingEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody User user) {

        User created = orchestrator.createUserForExistingEmployee(employeeId, user);
        ApiResponse<User> response = new ApiResponse<>("User created for employee successfully.", created,
                HttpStatus.CREATED.value(), true);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Employee employee) {
        Employee updatedEmployee = orchestrator.update(employee);
        ApiResponse<Employee> response = new ApiResponse<>("Employee updated successfully.", updatedEmployee,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/assign-company/{companyId}/{employeeId}")
    public ResponseEntity<?> assignCompany(@PathVariable Long companyId, @PathVariable Long employeeId) {
        Employee updatedEmployee = orchestrator.assignCompany(companyId, employeeId);
        ApiResponse<Employee> response = new ApiResponse<>("Employee assigned to company successfully.",
                updatedEmployee, HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/remove-company/{employeeId}")
    public ResponseEntity<?> removeCompany(@PathVariable Long employeeId) {
        Employee updatedEmployee = orchestrator.removeCompany(employeeId);
        ApiResponse<Employee> response = new ApiResponse<>("Employee company removed successfully.", updatedEmployee,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/assign-user/{userId}/{employeeId}")
    public ResponseEntity<?> assignUser(@PathVariable Long userId, @PathVariable Long employeeId) {
        Employee updatedEmployee = orchestrator.assignUser(userId, employeeId);
        ApiResponse<Employee> response = new ApiResponse<>("Employee assigned to user successfully.", updatedEmployee,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/remove-user/{employeeId}")
    public ResponseEntity<?> removeUser(@PathVariable Long employeeId) {
        Employee updatedEmployee = orchestrator.removeUser(employeeId);
        ApiResponse<Employee> response = new ApiResponse<>("Employee user removed successfully.", updatedEmployee,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Employee deleted = orchestrator.delete(id);
        ApiResponse<Employee> response = new ApiResponse<>("Employee deleted successfully.", deleted,
                HttpStatus.OK.value(), true);
        return ResponseEntity.ok().body(response);
    }

}

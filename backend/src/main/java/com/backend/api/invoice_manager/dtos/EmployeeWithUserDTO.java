package com.backend.api.invoice_manager.dtos;

import com.backend.api.invoice_manager.entities.Employee;
import com.backend.api.invoice_manager.entities.User;

import jakarta.validation.Valid;

public class EmployeeWithUserDTO {
    @Valid
    private Employee employee;

    @Valid
    private User user;

    public Employee getEmployee() { 
        return employee; 
    }

    public void setEmployee(Employee employee) {
        this.employee = employee; 
    }

    public User getUser() {
        return user; 
    }
    
    public void setUser(User user) {
        this.user = user; 
    }
}

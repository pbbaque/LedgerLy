package com.backend.api.invoice_manager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank.")
    @Size(min = 5, max = 30, message = "Product name must be between 5 and 30 characters.")
    private String name;

    @NotNull(message = "Product price cannot be null.")
    @Min(value = 500, message = "Product price must be greater than or equal to 500.")
    @Max(value = 1000000, message = "Product price must be less than or equal to 1,000,000.")
    private Double price;

    @NotBlank(message = "Product description cannot be blank.")
    @Size(min = 10, max = 100, message = "Product description must be between 10 and 100 characters.")
    private String description;

    @Pattern(regexp = "^[A-Z0-9]{6,12}$", message = "SKU must be 6 to 12 characters long and contain only uppercase letters and numbers.")
    @NotBlank(message = "Product SKU cannot be blank.")
    @Column(unique = true)
    private String sku;

    @Min(value = 0, message = "Stock cannot be negative.")
    @Max(value = 10000, message = "Stock cannot exceed 10,000.")
    @NotNull(message = "Stock cannot be null.")
    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @JsonIgnoreProperties({ "handler", "hibernateLazyInitializer", "company", "address", "employees" })
    private Company company;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }   
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }

    
}

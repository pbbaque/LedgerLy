package com.backend.api.invoice_manager.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.api.invoice_manager.dtos.TopProductDTO;
import com.backend.api.invoice_manager.entities.Product;
import com.backend.api.invoice_manager.responses.ApiResponse;
import com.backend.api.invoice_manager.services.orchestrators.ProductOrchestrator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductOrchestrator orchestrator;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Product> products = orchestrator.findAll();
        return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully.", products, 200, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Product product = orchestrator.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("Product retrieved successfully.", product, 200, true));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> findByCompanyId(@PathVariable Long companyId) {
        List<Product> products = orchestrator.findByCompanyId(companyId);
        return ResponseEntity.ok(new ApiResponse<>("Products retrieved successfully.", products, 200, true));
    }

    @GetMapping("/top")
    public ResponseEntity<?> getTopProducts() {
        List<TopProductDTO> topProducts = orchestrator.getTopProducts();
        return ResponseEntity.ok(new ApiResponse<>("Top products retrieved successfully.", topProducts, 200, true));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product product) {
        Product created = orchestrator.create(product);
        return ResponseEntity.status(201).body(new ApiResponse<>("Product created successfully.", created, 201, true));
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody Product product) {
        Product updated = orchestrator.update(product);
        return ResponseEntity.ok(new ApiResponse<>("Product updated successfully.", updated, 200, true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Product deleted = orchestrator.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Product deleted successfully.", deleted, 200, true));
    }
}

package com.backend.api.invoice_manager.services.orchestrators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.api.invoice_manager.dtos.TopProductDTO;
import com.backend.api.invoice_manager.entities.Product;
import com.backend.api.invoice_manager.services.orchestrators.permission.PermissionOrchestrator;
import com.backend.api.invoice_manager.services.product.ProductService;

@Component
public class ProductOrchestrator {

    @Autowired
    private ProductService productService;

    @Autowired
    private PermissionOrchestrator permissionOrchestrator;

    public List<Product> findAll() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return productService.findByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return productService.findAll();
    }

    public Product findById(Long id) {
        Product product = productService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(product.getCompany().getId());
        return product;
    }

    public List<Product> findByCompanyId(Long companyId) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(companyId);
        return productService.findByCompanyId(companyId);
    }

    public List<TopProductDTO> getTopProducts() {
        if (!permissionOrchestrator.isSuperAdminOrAdmin())
            return productService.getTopProductsByCompanyId(permissionOrchestrator.getCurrentCompany().getId());
        return productService.getTopProducts();
    }

    public Product create(Product product) {
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(product.getCompany().getId());
        return productService.save(product);
    }

    public Product update(Product product) {
        Product existing = productService.findById(product.getId());
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getCompany().getId());
        return productService.update(product);
    }

    public Product delete(Long id) {
        Product existing = productService.findById(id);
        permissionOrchestrator.requireCompanyOwnershipOrAdmin(existing.getCompany().getId());
        return productService.delete(id);
    }
}

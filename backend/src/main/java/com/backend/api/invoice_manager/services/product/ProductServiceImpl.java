package com.backend.api.invoice_manager.services.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.api.invoice_manager.dtos.TopProductDTO;
import com.backend.api.invoice_manager.entities.Product;
import com.backend.api.invoice_manager.exceptions.AlreadyExistsException;
import com.backend.api.invoice_manager.exceptions.NotFoundException;
import com.backend.api.invoice_manager.repositories.ProductRepository;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySkuAndCompanyId(String sku, Long companyId) {
        return repository.existsBySkuAndCompanyId(sku, companyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        List<Product> products = (List<Product>) repository.findAll();
        if (products.isEmpty())
            throw new NotFoundException("No products found.");
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCompanyId(Long companyId) {
        List<Product> products = repository.findByCompanyId(companyId);
        if (products.isEmpty())
            throw new NotFoundException("No products found for company with id: " + companyId);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopProductDTO> getTopProducts() {
        
        return repository.findTopProducts();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TopProductDTO> getTopProductsByCompanyId(Long companyId) {
        return repository.findTopProductsByCompanyId(companyId);
    }

    @Override
    public Product save(Product product) {
        if (product.getCompany() == null) {
            throw new NotFoundException("Company must be specified for product creation.");
        }

        if (existsBySkuAndCompanyId(product.getSku(), product.getCompany().getId())) {
            throw new AlreadyExistsException(
                "Product with SKU " + product.getSku() + " already exists in this company."
            );
        }

        return repository.save(product);
    }

    @Override
    public Product update(Product product) {
        Product existing = findById(product.getId());

        if (!existing.getSku().equals(product.getSku())) {
            if (existsBySkuAndCompanyId(product.getSku(), existing.getCompany().getId())) {
                throw new AlreadyExistsException(
                    "Product with SKU " + product.getSku() + " already exists in this company."
                );
            }
        }

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setSku(product.getSku());
        existing.setStock(product.getStock());
        existing.setCompany(product.getCompany());

        return repository.save(existing);
    }

    @Override
    public Product delete(Long id) {
        Product existing = findById(id);
        repository.delete(existing);
        return existing;
    }
}

package com.backend.api.invoice_manager.services.product;

import java.util.List;

import com.backend.api.invoice_manager.dtos.TopProductDTO;
import com.backend.api.invoice_manager.entities.Product;

public interface ProductService {

    boolean existsBySku(String sku);

    boolean existsBySkuAndCompanyId(String sku, Long companyId);

    List <Product> findAll();

    List<Product> findByCompanyId(Long companyId);

    Product findById(Long id);

    List<TopProductDTO> getTopProducts();

    List<TopProductDTO> getTopProductsByCompanyId(Long companyId);

    Product save(Product product);

    Product update(Product product);

    Product delete(Long id);
}

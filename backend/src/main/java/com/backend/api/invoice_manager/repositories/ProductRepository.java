package com.backend.api.invoice_manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import com.backend.api.invoice_manager.dtos.TopProductDTO;
import com.backend.api.invoice_manager.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

  @Override
  @EntityGraph(attributePaths = { "company" })
  @NonNull
  List<Product> findAll();

  @Override
  @EntityGraph(attributePaths = { "company" })
  @NonNull
  Optional<Product> findById(@NonNull Long id);

  @EntityGraph(attributePaths = { "company" })
  boolean existsBySku(String sku);

  @EntityGraph(attributePaths = { "company" })
  boolean existsBySkuAndCompanyId(String sku, Long companyId);

  @EntityGraph(attributePaths = { "company" })
  List<Product> findByCompanyId(Long companyId);

  // Search general
  @Query("""
          SELECT p FROM Product p
          LEFT JOIN FETCH p.company c
          WHERE (:term IS NULL
              OR LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
              OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
              OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :term, '%'))
              OR LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%'))
          )
      """)
  List<Product> searchProducts(@Param("term") String term);

  // Search filtered by company
  @Query("""
          SELECT p FROM Product p
          LEFT JOIN FETCH p.company c
          WHERE c.id = :companyId
            AND (:term IS NULL
              OR LOWER(p.name) LIKE LOWER(CONCAT('%', :term, '%'))
              OR LOWER(p.description) LIKE LOWER(CONCAT('%', :term, '%'))
              OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :term, '%'))
            )
      """)
  List<Product> searchProductsByCompany(@Param("term") String term, @Param("companyId") Long companyId);

  @Query(value = """
          SELECT p.name AS name,
                 SUM(d.quantity) AS quantitySold
          FROM products p
          JOIN invoice_details d ON p.id = d.product_id
          GROUP BY p.id, p.name
          ORDER BY quantitySold DESC
          LIMIT 5
      """, nativeQuery = true)
  List<TopProductDTO> findTopProducts();

  @Query(value = """
          SELECT p.name AS name,
                 SUM(d.quantity) AS quantitySold
          FROM products p
          JOIN invoice_details d ON p.id = d.product_id
          WHERE p.company_id = :companyId
          GROUP BY p.id, p.name
          ORDER BY quantitySold DESC
          LIMIT 5
      """, nativeQuery = true)
  List<TopProductDTO> findTopProductsByCompanyId(@Param("companyId")Long companyId);

}

package com.mojo.cart.product.repository;

import com.mojo.cart.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    @EntityGraph(attributePaths = {"category", "variants", "images", "productDetails"})
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Product findProductWithDetailsById(UUID id);
    
    @EntityGraph(attributePaths = {"category", "variants", "images", "productDetails"})
    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    List<Product> findAllActiveProductsWithDetails();
    
    List<Product> findByCategoryIdAndIsActive(UUID categoryId, Boolean isActive);
    
    List<Product> findByBrandAndIsActive(String brand, Boolean isActive);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchActiveProducts(String keyword);
    
    boolean existsByName(String name);
}

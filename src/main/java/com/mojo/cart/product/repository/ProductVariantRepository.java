package com.mojo.cart.product.repository;

import com.mojo.cart.product.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, UUID> {
    
    @EntityGraph(attributePaths = {"product", "attributes", "images"})
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.id = :id")
    ProductVariant findVariantWithDetailsById(UUID id);
    
    List<ProductVariant> findByProductIdAndIsActive(UUID productId, Boolean isActive);
    
    boolean existsBySku(String sku);
    
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.stock > 0 AND pv.isActive = true")
    List<ProductVariant> findInStockVariants();
}

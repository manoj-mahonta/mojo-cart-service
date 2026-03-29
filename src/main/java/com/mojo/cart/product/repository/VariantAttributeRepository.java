package com.mojo.cart.product.repository;

import com.mojo.cart.product.entity.VariantAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VariantAttributeRepository extends JpaRepository<VariantAttribute, UUID> {
    
    List<VariantAttribute> findByVariantId(UUID variantId);
    
    List<VariantAttribute> findByVariantIdAndAttributeName(UUID variantId, String attributeName);
}

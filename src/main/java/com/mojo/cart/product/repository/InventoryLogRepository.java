package com.mojo.cart.product.repository;

import com.mojo.cart.product.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, UUID> {
    
    List<InventoryLog> findByVariantIdOrderByCreatedAtDesc(UUID variantId);
    
    @Query("SELECT il FROM InventoryLog il WHERE il.variant.id = :variantId AND il.changeType = :changeType ORDER BY il.createdAt DESC")
    List<InventoryLog> findByVariantIdAndChangeTypeOrderByCreatedAtDesc(UUID variantId, InventoryLog.ChangeType changeType);
}

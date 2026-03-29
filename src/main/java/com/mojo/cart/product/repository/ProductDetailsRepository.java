package com.mojo.cart.product.repository;

import com.mojo.cart.product.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, UUID> {
}

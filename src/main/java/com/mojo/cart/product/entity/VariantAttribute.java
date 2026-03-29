package com.mojo.cart.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "variant_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttribute {
    
    @Id
    @UuidGenerator
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
    
    @Column(name = "attribute_name", nullable = false)
    private String attributeName;
    
    @Column(name = "attribute_value", nullable = false)
    private String attributeValue;
}

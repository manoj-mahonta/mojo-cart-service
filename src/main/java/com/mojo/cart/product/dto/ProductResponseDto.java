package com.mojo.cart.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    
    private UUID id;
    private String name;
    private String shortDescription;
    private String brand;
    private CategoryDto category;
    private Boolean isActive;
    private LocalDateTime createdAt;
    
    private ProductDetailsDto productDetails;
    private List<VariantResponseDto> variants;
    private List<ImageResponseDto> images;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailsDto {
        private UUID id;
        private String description;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantResponseDto {
        private UUID id;
        private String sku;
        private Double price;
        private Integer stock;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private List<AttributeResponseDto> attributes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeResponseDto {
        private UUID id;
        private String attributeName;
        private String attributeValue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageResponseDto {
        private UUID id;
        private String imageUrl;
        private Boolean isPrimary;
        private UUID variantId;
    }
}

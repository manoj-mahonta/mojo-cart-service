package com.mojo.cart.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must not exceed 255 characters")
    private String name;
    
    @Size(max = 500, message = "Short description must not exceed 500 characters")
    private String shortDescription;
    
    @Size(max = 100, message = "Brand must not exceed 100 characters")
    private String brand;
    
    @NotNull(message = "Category ID is required")
    private UUID categoryId;
    
    private String description;
    
    @Valid
    private List<VariantRequestDto> variants;
    
    @Valid
    private List<ImageRequestDto> images;
}

package com.mojo.cart.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeRequestDto {
    
    @NotBlank(message = "Attribute name is required")
    @Size(max = 50, message = "Attribute name must not exceed 50 characters")
    private String attributeName;
    
    @NotBlank(message = "Attribute value is required")
    @Size(max = 100, message = "Attribute value must not exceed 100 characters")
    private String attributeValue;
}

package com.mojo.cart.product.service;

import com.mojo.cart.product.dto.*;
import com.mojo.cart.product.entity.*;
import com.mojo.cart.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final ProductVariantRepository productVariantRepository;
    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductImageRepository productImageRepository;
    private final InventoryLogRepository inventoryLogRepository;
    
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + requestDto.getCategoryId()));
        
        if (productRepository.existsByName(requestDto.getName())) {
            throw new IllegalArgumentException("Product with name '" + requestDto.getName() + "' already exists");
        }
        
        Product product = new Product();
        product.setName(requestDto.getName());
        product.setShortDescription(requestDto.getShortDescription());
        product.setBrand(requestDto.getBrand());
        product.setCategory(category);
        product.setIsActive(true);
        
        Product savedProduct = productRepository.save(product);
        
        if (requestDto.getDescription() != null && !requestDto.getDescription().trim().isEmpty()) {
            ProductDetails productDetails = new ProductDetails();
            productDetails.setProduct(savedProduct);
            productDetails.setDescription(requestDto.getDescription());
            productDetailsRepository.save(productDetails);
        }
        
        if (requestDto.getVariants() != null && !requestDto.getVariants().isEmpty()) {
            for (VariantRequestDto variantDto : requestDto.getVariants()) {
                createProductVariant(savedProduct, variantDto);
            }
        }
        
        if (requestDto.getImages() != null && !requestDto.getImages().isEmpty()) {
            for (ImageRequestDto imageDto : requestDto.getImages()) {
                createProductImage(savedProduct, null, imageDto);
            }
        }
        
        return getProductById(savedProduct.getId());
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAllActiveProductsWithDetails().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        Product product = productRepository.findProductWithDetailsById(id);
        if (product == null) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        return convertToResponseDto(product);
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(UUID categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found with id: " + categoryId);
        }
        
        return productRepository.findByCategoryIdAndIsActive(categoryId, true).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductResponseDto> searchProducts(String keyword) {
        return productRepository.searchActiveProducts(keyword).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    private ProductVariant createProductVariant(Product product, VariantRequestDto variantDto) {
        if (productVariantRepository.existsBySku(variantDto.getSku())) {
            throw new IllegalArgumentException("Variant with SKU '" + variantDto.getSku() + "' already exists");
        }
        
        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setSku(variantDto.getSku());
        variant.setPrice(variantDto.getPrice());
        variant.setStock(variantDto.getStock());
        variant.setIsActive(true);
        
        ProductVariant savedVariant = productVariantRepository.save(variant);
        
        if (variantDto.getAttributes() != null && !variantDto.getAttributes().isEmpty()) {
            for (AttributeRequestDto attributeDto : variantDto.getAttributes()) {
                createVariantAttribute(savedVariant, attributeDto);
            }
        }
        
        createInventoryLog(savedVariant, InventoryLog.ChangeType.IN, variantDto.getStock());
        
        return savedVariant;
    }
    
    private VariantAttribute createVariantAttribute(ProductVariant variant, AttributeRequestDto attributeDto) {
        VariantAttribute attribute = new VariantAttribute();
        attribute.setVariant(variant);
        attribute.setAttributeName(attributeDto.getAttributeName());
        attribute.setAttributeValue(attributeDto.getAttributeValue());
        return variantAttributeRepository.save(attribute);
    }
    
    private ProductImage createProductImage(Product product, ProductVariant variant, ImageRequestDto imageDto) {
        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setVariant(variant);
        image.setImageUrl(imageDto.getImageUrl());
        image.setIsPrimary(imageDto.getIsPrimary());
        return productImageRepository.save(image);
    }
    
    private InventoryLog createInventoryLog(ProductVariant variant, InventoryLog.ChangeType changeType, Integer quantity) {
        InventoryLog log = new InventoryLog();
        log.setVariant(variant);
        log.setChangeType(changeType);
        log.setQuantity(quantity);
        return inventoryLogRepository.save(log);
    }
    
    private ProductResponseDto convertToResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setShortDescription(product.getShortDescription());
        dto.setBrand(product.getBrand());
        dto.setIsActive(product.getIsActive());
        dto.setCreatedAt(product.getCreatedAt());
        
        if (product.getCategory() != null) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            categoryDto.setParentId(product.getCategory().getParentId());
            dto.setCategory(categoryDto);
        }
        
        if (product.getProductDetails() != null) {
            ProductResponseDto.ProductDetailsDto detailsDto = new ProductResponseDto.ProductDetailsDto();
            detailsDto.setId(product.getProductDetails().getId());
            detailsDto.setDescription(product.getProductDetails().getDescription());
            dto.setProductDetails(detailsDto);
        }
        
        if (product.getVariants() != null) {
            List<ProductResponseDto.VariantResponseDto> variantDtos = product.getVariants().stream()
                    .map(this::convertVariantToDto)
                    .collect(Collectors.toList());
            dto.setVariants(variantDtos);
        }
        
        if (product.getImages() != null) {
            List<ProductResponseDto.ImageResponseDto> imageDtos = product.getImages().stream()
                    .map(this::convertImageToDto)
                    .collect(Collectors.toList());
            dto.setImages(imageDtos);
        }
        
        return dto;
    }
    
    private ProductResponseDto.VariantResponseDto convertVariantToDto(ProductVariant variant) {
        ProductResponseDto.VariantResponseDto dto = new ProductResponseDto.VariantResponseDto();
        dto.setId(variant.getId());
        dto.setSku(variant.getSku());
        dto.setPrice(variant.getPrice());
        dto.setStock(variant.getStock());
        dto.setIsActive(variant.getIsActive());
        dto.setCreatedAt(variant.getCreatedAt());
        
        if (variant.getAttributes() != null) {
            List<ProductResponseDto.AttributeResponseDto> attributeDtos = variant.getAttributes().stream()
                    .map(this::convertAttributeToDto)
                    .collect(Collectors.toList());
            dto.setAttributes(attributeDtos);
        }
        
        return dto;
    }
    
    private ProductResponseDto.AttributeResponseDto convertAttributeToDto(VariantAttribute attribute) {
        ProductResponseDto.AttributeResponseDto dto = new ProductResponseDto.AttributeResponseDto();
        dto.setId(attribute.getId());
        dto.setAttributeName(attribute.getAttributeName());
        dto.setAttributeValue(attribute.getAttributeValue());
        return dto;
    }
    
    private ProductResponseDto.ImageResponseDto convertImageToDto(ProductImage image) {
        ProductResponseDto.ImageResponseDto dto = new ProductResponseDto.ImageResponseDto();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setIsPrimary(image.getIsPrimary());
        if (image.getVariant() != null) {
            dto.setVariantId(image.getVariant().getId());
        }
        return dto;
    }
}

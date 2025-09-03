package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto
{
    @NotBlank(message = "product name is required")
    private String name;
    private String description;
    @Min(value = 0,message = "stock quantity must be positive")
    private int stockQuantity;
    @NotBlank
    @Min(value = 0, message = "price must be positive")
    private BigDecimal price;
    private Long categoryId;
}

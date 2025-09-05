package com.DipYukti.Ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto
{
    @NotBlank(message = "product name is required")
    private String name;
    private String description;
    @NotNull
    @Min(value = 0,message = "stock quantity must be positive")
    private Integer stockQuantity;
    @NotNull
    @Min(value = 0, message = "price must be positive")
    private BigDecimal price;
    private Long categoryId;
}
